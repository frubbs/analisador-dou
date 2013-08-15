package exec;

public class teste
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int v1[] =
		{ 1, 2, 3, 4, 5 };

		for (int i = 0; i < v1.length; i++)
		{
			for (int j = i + 1; j < v1.length; j++)
			{
				System.out.println(v1[i] + "-" + v1[j]);
			}
		}

	}

}
