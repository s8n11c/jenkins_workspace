public class Rectangulator{
	

	public static void main(String[] args){
		int length=Integer.parseInt(args[0]);
		int width=Integer.parseInt(args[1]);
		Rectangle r=new Rectangle(length,width);

		System.out.println(r.getArea());
	}

}
