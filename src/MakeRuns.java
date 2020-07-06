//	Liam Chandler	ID:	1286559
//	Daniel Bartley  ID: 1331132

import java.io.*;

public class MakeRuns
{
	private BufferedWriter currentBw, out1Bw, out2Bw;
	private File out1, out2;
	private int currentLine, heapSize;
	private heapSort heap;

	MakeRuns(int runSize, String fileName)
	{
		// Setup heap.
		heapSize = runSize;
		heap = new heapSort(heapSize);

		// Define output files
		out1 = new File("out1.txt");
		out2 = new File("out2.txt");
		BufferedReader br;

		try
		{
			// Check if the files exist if not make them.
			if (!out1.exists())
				out1.createNewFile();
			if (!out2.exists())
				out2.createNewFile();

			// Open the output files
			out1Bw = new BufferedWriter(new FileWriter(out1));
			out2Bw = new BufferedWriter(new FileWriter(out2));

			// Open the given input file.
			String[] tokens;
			br = new BufferedReader(new FileReader(fileName));
			String s = br.readLine();

			// While there is a next line.
			while (s != null)
			{
				tokens = s.split(" ");
				for (String S : tokens)
				{
					if (currentLine % heapSize == 0)    // If at run size make new run
						writeRun();
					heap.add(S);    // Write current word to heap
					currentLine++;
				}
				s = br.readLine();  // Read next line
			}
			writeRun(); // Close final run.
			br.close();
			out1Bw.close();
			out2Bw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void writeRun() throws IOException    // Dumps heap into file and changes while file is being written to.
	{
		try
		{
			// Set the current bufferedwriter if there is none.
			if (currentBw == null)
				currentBw = out1Bw;
			else
			{
				while (heap.getSize() >= 0)
					currentBw.write(heap.remove() + "\n");   // Write current item to file
				currentBw.flush();

				//  Swap file
				if (currentBw == out1Bw)
					currentBw = out2Bw;
				else
					currentBw = out1Bw;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

class heapSort  // Create and maintain heap during adding and removing of items.
{
	private String[] heap;  // heap Array.
	private int count = 0;  // count of items in heap

	heapSort(int size)  // Create heap of the given size
	{
		heap = new String[size+2];
	}

	public void add(String New)     // Add the given item to the heap and call for it to be rearranged.
	{
		heap[count] = New;
		upHeapify(count);
		count++;
	}

	public String remove()      // Return the most important item in the heap and call for it to rearrange the remaining items
	{
		count--;
		heap[heap.length-1] = heap[0];  // Move the top item to the last position.
		heap[0] = heap[count];          // Make the last item in the first position.
		downHeapify(0);             // Rearrange the items in the heap.

		return heap[heap.length-1];
	}

	private void upHeapify(int index)   // Recursively check if the current item is more important than the parent if so reorder them.
	{
		if(index != 0)  // Check if there is a parent.
		{
			int parentIndex = (index -1)/2;
			// Check if the child is more important than the parent.
			int comparison = heap[parentIndex].toLowerCase().compareTo(heap[index].toLowerCase());
			if(comparison > 0)
			{
				// Swap parent and child.
				heap[count+1] = heap[parentIndex];
				heap[parentIndex] = heap[index];
				heap[index] = heap[count+1];

				// Recurse
				upHeapify(parentIndex);
			}
		}
	}

	private void downHeapify(int index) // Recursively check if ether child is more important than the parent if so reorder them.
	{
		if (index < count / 2) // Check if current item has a child.
		{
			int child1Index = index * 2 + 1, child2Index = index * 2 + 2;   // Calculate children's index
			// Check if child2 exists.
			if (heap[child2Index] == null || child2Index > count)
			{
				heap[index] = heap[child1Index];
				return;
			}

			String tmp; // tmp variable for rearranging heap.
			if (heap[index].toLowerCase().compareTo(heap[child1Index].toLowerCase()) > 0 || heap[index].toLowerCase().compareTo(heap[child2Index].toLowerCase()) > 0)   // Check if a child needs to be swapped.
			{
				// Check which child needs to be swapped
				if (heap[child1Index].toLowerCase().compareTo(heap[child2Index].toLowerCase()) > 0)
				{
					tmp = heap[index];                  // Store parent in tmp.
					heap[index] = heap[child2Index];    // move child to parents position
					heap[child2Index] = tmp;            // restore parent to child's position
					downHeapify(child2Index);           // Recurse on changed item
				} else
				{
					tmp = heap[index];                  // See above.
					heap[index] = heap[child1Index];
					heap[child1Index] = tmp;
					downHeapify(child1Index);
				}
			}
		}
	}

	public int getSize()
	{
		return count-1;
	}
}