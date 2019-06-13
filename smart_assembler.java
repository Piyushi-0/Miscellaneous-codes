import java.util.*;
class P{
public static void main(String args[])
	{
	StringBuilder ab=new StringBuilder("");
	String s1, s2;
	Scanner sc=new Scanner(System.in);
	/*do	
		{
		s2=sc.nextLine();
		ab.append(s2);
		if(s2.length()>0)
			ab.append("\n");
		}while(s2.length()>0);
	
	String inp=ab.toString();*/
	
	String inp="SET a 0\nLABEL 100\nADD a 1 a\nECHO a\nIF a 5\nSET a 9\nIF 2 1\nECHO a\nCONTINUE\nEND\nEXIT\nEND\nGOTO 100";
	//"SET a 0\nLABEL 100\nADD a 1 a\nECHO a\nIF a 5\nEXIT\nEND\nGOTO 100";
	HashMap<String, Integer> om=new HashMap<String, Integer>();//operand mapping to integer value
	Stack<Integer> ifc=new Stack<Integer>();//the most recent if for corresponding continue
	int j=0, x=0, x1=0;
	List<String> l=new LinkedList<String>();//lines of input in index-wise in LL
	for(String s:inp.split("\n"))
		l.add(s);
		
	while(j<l.size())
		{//System.out.println(ifc);
		s1=l.get(j);//one instruction at a time
		if(s1.length()==0)
			{
			j++;
			continue;
			}
		ArrayList<String> a=new ArrayList<String>();
		for(String s:s1.split(" "))//parts of instruction
			a.add(s);//s1 no longer needed
			
		if(a.get(0).equals("LABEL"))
			{//next could be integer or variable  /*char c=a.get(1).charAt(0); if(Character.isDigit(c))lm.put((int)c, j);if(Character.isLetter(c))lm.put(om.get(c), j);*/
			j++;			}
			
		if(a.get(0).equals("GOTO"))//LABEL MAY BE USED AFTER USING GOTO=>search for that label whenever reach
			{				
			int i; s1=a.get(1);
			try{
				x=Integer.parseInt(s1);
				}
			catch(NumberFormatException e)
				{//GOTO TARGET IN VARIABLE
				x=om.get(s1);
				}
			for(i=0;i<l.size();i++)
				{
				if(l.get(i).split(" ")[0].equals("LABEL"))
					{
					s1=l.get(i).split(" ")[1];
					try{
						x1=Integer.parseInt(s1);
						}
					catch(NumberFormatException e)
						{
						x1=om.get(s1);
						}
					if(x==x1)
						{
						j=i+1;//next of that label
						}
					}
				}
			}

		if(a.get(0).equals("IF"))//CONTINUE END
			{
			String is1=a.get(1), is2=a.get(2);
			try{
			x1=Integer.parseInt(is1);
			}
			catch(NumberFormatException e)
			{x1=om.get(is1);
			}
			try{
			x=Integer.parseInt(is2);
			}
			catch(NumberFormatException e)
			{x=om.get(is2);
			}
			if(x1==x)
				{
				ifc.push(j);//last if instance
				j++;//move to next instruction
				}
			else
				{x1=1;
				do{
				j++;
				s1=l.get(j);
				if(s1.split(" ")[0].equals("IF"))
					x1++;						
				if(s1.equals("END"))
					x1--;
					}while(x1>0);
				j++;
				}
			}

			
		if(a.get(0).equals("SET"))
			{
			s1=a.get(2);//second operand
			try{
				x=Integer.parseInt(s1);
				}
			catch(Exception e)//second operand is a variable
				{
				x=om.get(s1);
				}
			if(om.containsKey(a.get(1))==false)
				{
				om.put(a.get(1), x);
				}
			else
				om.replace(a.get(1), x);
			j++;
			}
			
		if(a.get(0).equals("ADD"))
			{
			String lab=a.get(3); 
			s1=a.get(1); s2=a.get(2);
			try{
			x1=Integer.parseInt(s1);
			}
			catch(NumberFormatException e){//it's in a variable
			x1=om.get(s1);
			}
			try{
			x=Integer.parseInt(s2);
			}
			catch(NumberFormatException e){
			x=om.get(s2);
			}
			if(om.containsKey(lab)==false)
				{
				om.put(lab,(x1+x));
				}
			else
				om.replace(lab,(x1+x));
			j++;
			}
			
		if(a.get(0).equals("ECHO"))
			{
			s1=a.get(1);
			try{
			x=Integer.parseInt(s1);
			System.out.println(x);	
			}
			catch(NumberFormatException e)
				{//variable
				x=om.get(s1);
				System.out.println(x);
				}
			j++;
			}
			
		if(a.get(0).equals("CONTINUE"))
			if(ifc.size()>0)
				j=ifc.pop();
		if(a.get(0).equals("END"))
			{
			if(ifc.size()>0)
				ifc.pop();//last if record deleted
			j++;
			}
		if(a.get(0).equals("EXIT"))
			System.exit(0);
		}
   	}
}