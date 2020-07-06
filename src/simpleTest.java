
class simpleTest
{
    public static void main(String[] args)
    {
        int runSize = 10000;
        new MakeRuns(runSize,"BrownCorpus.txt");
        new SortMerge(runSize);
    }
}