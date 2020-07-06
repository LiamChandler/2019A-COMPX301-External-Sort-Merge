//	Liam Chandler	ID:	1286559
//	Daniel Bartley  ID: 1331132

import java.io.*;

class SortMerge
{
	private static int outputCount = 0; // number of times any file is opened for output
	SortMerge(int runSize)
	{
		makeBiggerRuns(runSize);

		System.err.println("Output files opened = " + outputCount);
	}

	private void makeBiggerRuns(int runSize)    // sort two runs into one run with twice the length
	{
		// rearrange files for recursive calls
		File in1 = new File("in1.txt");
		if (in1.exists())
			in1.delete();
		File out1 = new File("out1.txt");
		out1.renameTo(in1); // making the previous output file 1 the new input file 1

		File in2 = new File("in2.txt");
		if (in2.exists())
			in2.delete();
		File out2 = new File("out2.txt");
		out2.renameTo(in2); // making the previous output file 2 the new input file 2

		String topString1, topString2;
		int in1Count = 0, in2Count = 0;

		try
		{
			BufferedReader input1Br = new BufferedReader(new FileReader(in1));
			BufferedReader input2Br = new BufferedReader(new FileReader(in2));

			outputCount += 2;   // 2 files are opened in the following lines
			BufferedWriter output1Bw = new BufferedWriter(new FileWriter(out1));
			BufferedWriter output2Bw = new BufferedWriter(new FileWriter(out2));
			BufferedWriter currentBw = output1Bw;   // setting initial output file

			topString1 = input1Br.readLine();
			topString2 = input2Br.readLine();

			Merge(runSize, topString1, topString2, in1Count, in2Count, input1Br, input2Br, currentBw, output1Bw, output2Bw); // merge the two files

			input1Br.close();   // close all used files
			input2Br.close();
			output1Bw.close();
			output2Bw.close();

			BufferedReader tmp1 = new BufferedReader(new FileReader(out1)), tmp2 = new BufferedReader(new FileReader(out2));
			String line = tmp1.readLine();  // temporary variable to store the first item in the sorted file when it needs to be printed out
			if(line != null && tmp2.readLine() != null) // check that there is still input in both files
			{
				tmp1.close();
				tmp2.close();
				makeBiggerRuns(runSize*2);  // recurse with larger run size
			}
			else
			{
				while(line != null){
					System.out.println(line);   // print out sorted file to console
					line = tmp1.readLine();
				}
				tmp1.close();
				tmp2.close();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	// Checks which item needs to be put into the output file next.
	private void Merge(int runSize, String topString1, String topString2, int in1Count, int in2Count, BufferedReader input1Br, BufferedReader input2Br, BufferedWriter currentBw, BufferedWriter output1Bw, BufferedWriter output2Bw) throws IOException
	{
		while (in1Count < runSize || in2Count < runSize)    // make sure that it stops at the end of the current run.
		{
			if(topString1 == null && topString2 == null)    // if there are no new words, break.
				break;

			if (topString2 == null || in2Count >= runSize) // if input file 2 is empty output the remains of input file 1
			{
				currentBw.write(topString1 + "\n");
				topString1 = input1Br.readLine();
				in1Count++;
			} else if (topString1 == null || in1Count >= runSize)   // if input file 1 is empty output the remains of input file 2
			{
				currentBw.write(topString2 + "\n");
				topString2 = input2Br.readLine();
				in2Count++;

			} else
			{
				int tmp = topString1.toLowerCase().compareTo(topString2.toLowerCase()); //compare the top two words
				// output the most important word and find the next word
				if (tmp < 0)
				{
					currentBw.write(topString1 + "\n");
					topString1 = input1Br.readLine();
					in1Count++;
				} else
				{
					currentBw.write(topString2 + "\n");
					topString2 = input2Br.readLine();
					in2Count++;
				}
			}
		}
		if (topString1 != null || topString2 != null)   // check if there is still input left in either file
		{
			in1Count = 0;
			in2Count = 0;
			// swap output file
			if (currentBw == output1Bw)
				currentBw = output2Bw;
			else
				currentBw = output1Bw;
			// begin sorting next pair of runs
			Merge(runSize, topString1, topString2, in1Count, in2Count, input1Br, input2Br, currentBw, output1Bw, output2Bw);
		}
	}
}