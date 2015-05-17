package lxq.example.table;


public class LunTanInstance {
	private static LunTan lunTan;
	public static LunTan getLunTan()
	{
		if(lunTan==null)
		{
			lunTan=new LunTan();
		}
		return lunTan;
	}
	public static void setLunTan(LunTan l)
	{
		lunTan=l;
	}
}
