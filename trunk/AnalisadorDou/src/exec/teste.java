package exec;

import java.util.HashMap;

import dou.processador1modo.Entidade;

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
				// System.out.println(v1[i] + "-" + v1[j]);
			}
		}

		HashMap<String, Entidade> map = new HashMap<String, Entidade>();

		Entidade e1 = new Entidade("teste1", null, 0, 0, 0, "");
		map.put(e1.entidade, e1);

		Entidade e2 = new Entidade("teste4", null, 0, 0, 0, "");
		map.put(e2.entidade, e2);

		Entidade e3 = new Entidade("teste1", null, 0, 0, 0, "");
		map.put(e3.entidade, e3);

		Entidade e4 = new Entidade("teste4", null, 0, 0, 0, "");
		map.put(e4.entidade, e4);

		Entidade e5 = new Entidade("teste3", null, 0, 0, 0, "");
		map.put(e5.entidade, e5);

		Entidade e6 = new Entidade("teste4", null, 0, 0, 0, "");
		map.put(e6.entidade, e6);

		@SuppressWarnings("unchecked")
		Entidade[] keys = map.values().toArray(new Entidade[0]);

		System.out.println("tamano: " + keys.length);
		for (int i = 0; i < keys.length; i++)
		{
			for (int j = i + 1; j < keys.length; j++)
			{
				System.out.println(keys[i].entidade + "-" + keys[j].entidade);
			}
		}

	}

}
