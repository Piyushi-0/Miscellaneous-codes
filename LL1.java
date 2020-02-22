/*
Code for LL1 parser.
*/

import java.util.*;
class FT
{
static HashMap<String,String> hm=new HashMap <String,String>(), fs=new HashMap <String,String>(), fl=new HashMap <String,String>();
static boolean terminal(char ch)
	{
	if(!Character.isLetter(ch)||Character.isLowerCase(ch))
		return true;
	return false;
	}
static String first(String key)
	{
	if(fs.get(key).length()>0)
		return fs.get(key);
	String p=hm.get(key);
	String t="";
	for(String s:p.split("[|]"))
		{
		if(terminal(s.charAt(0)))
			{
			t+=s.charAt(0)+" ";
			}
		else//non-terminal
			{
			int x, d=-1;
			StringBuilder tmp=new StringBuilder(first(s.charAt(++d)+""));
			x=tmp.indexOf("e ");
			while(x>=0 && d<s.length()-1)
				{
				tmp.delete(x,x+2);
				tmp.append(first(s.charAt(++d)+""));
				x=tmp.indexOf("e ");
				}
			t+=tmp;
			}
		}
	fs.put(key,t);
	return t;
	}
static String follow(String key)//LOOP AVOIDANCE REMAINS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	{
	if(fl.get(key).length()>0)
		return fl.get(key);
	String p, t="";
	if(key.charAt(0)=='S')
		t+="$ ";
	for(String k:hm.keySet())
		{
		p=hm.get(k);
		int x=p.indexOf(key);
		if(x>=0)
			{
			if(x+1<p.length() && p.charAt(x+1)!='|')
				{
				char ch=p.charAt(x+1);
				if(terminal(ch))//our first not for direct terminals
					t+=ch+" ";//won't be epsilon
				else
					{//epsilon could come here
					StringBuilder tmp=new StringBuilder(first(ch+""));
					int i=tmp.indexOf("e "), d=x+1;
					while(i>=0 && d+1<p.length() && p.charAt(d+1)!='|')
						{
						tmp.deleteCharAt(i);
						tmp.append(first(p.charAt(++d)+""));
						i=tmp.indexOf("e ");
						}
					if(i>=0)
						tmp.append(follow(k));
					t+=tmp;
					}
				}
			else if(k.charAt(0)!=key.charAt(0))//Then follows it's LHS
				t+=follow(k);
			}
		}
	fl.put(key, t);	
	return t;
	}
public static void main(String args[])
	{//starts with s. In stack s to be initially
	hm.put("S","AB");fs.put("S","");fl.put("S","");
	hm.put("A","e|aA");fs.put("A","");fl.put("A","");
	hm.put("B","b|C");fs.put("B","");fl.put("B","");
	hm.put("C","i");fs.put("C","");fl.put("C","");
	int i, l=hm.size();
	HashMap <String, String> term=new HashMap <String, String>();
	String t="$ ";
	for(String s:hm.keySet())
		{
		for(i=0;i<hm.get(s).length();i++)
			{
			char ch=hm.get(s).charAt(i);
			if(ch!='e' && ch!='|' && terminal(ch))
				t+=ch+" ";//terminals
			}
		first(s);
		follow(s);
		}
	for(String s1:hm.keySet())	
		{
		for(String s:t.split(" "))	
			term.put(s1+s,"");
		}
	System.out.println("First of");
	for(String s:hm.keySet())
		{
		System.out.println(s+" is "+fs.get(s));
		}
	
	System.out.println("Follow of");
	for(String s:hm.keySet())
		{
		System.out.println(s+" is "+fl.get(s));
		}
	/*---------PARSING TABLE---------*/
	System.out.println("Parsing Table:");
	for(String s:t.split(" "))
		{
		System.out.print("  "+s);//terminals on top displayed
		}
	System.out.println("");
	for(String s:hm.keySet())
		{
		for(String f:fs.get(s).split(" "))//their corresponding first
			{
			char ch=f.charAt(0);//first
			if(ch=='e')
				{
				for(String p:fl.get(s).split(" "))//to all it's follow, add epsilon production
					{
					ch=p.charAt(0);
					term.replace(s+ch,"e");
					}
				}
			else
				{
				String str="";
				for(String p:hm.get(s).split("[|]"))//each individual production
					{
					if(p.charAt(0)==ch)//if direct matches terminal since our first not for terminal as input
						{
						if(!str.equals(""))
							str+=",";
						str+=p;//add that production
						}
					else if(!terminal(p.charAt(0)))//rejecting other terminals since our first not for terminal as input
						{
						for(i=0;i<p.length();i++)
							{
							if(fs.get(p.charAt(i)+"").contains(ch+""))//if it's production can give this terminal
								{
								if(!str.equals(""))
									str+=",";
								str+=p;
								}
							}
						}
					}
				term.replace(s+ch,str+" ");//to all it's first, add production...
				}
			}
		}
	for(String s1:hm.keySet())
		{
		System.out.print(s1+" ");
		for(String s2:t.split(" "))
			System.out.print(term.get(s1+s2)+" ");
		System.out.println("");		
		}
	/*------------------------------------------------------------------STRING ACCEPTANCE-------------------------------------------------------------------------------------*/	
	Stack <Character>S=new Stack<Character>();
	S.push('$');
	S.push('S');
	System.out.println("Enter a string for acceptance by the grammar");
	String inp=new Scanner(System.in).nextLine();
	inp+="$";
	for(i=0;i<inp.length();)
		{
		char d, c=inp.charAt(i);
		System.out.println("Stack"+S+"\t Input symbol "+c);
		if(c=='$' && S.peek()=='$')
			{
			System.out.println("Accepted");
			System.exit(0);
			}
		d=S.pop();
		if(inp.charAt(i)==d)
			i++;
		else
			{
			if(d=='$' || c=='$')
				{
				System.out.println("Not accepted");
				System.exit(0);
				}
			String tmp=term.get(d+""+c);
			if(tmp.trim().length()==0)
				{
				System.out.println("Not accepted");
				System.exit(0);
				}
			if(tmp.charAt(0)!='e')
				{
				int j;
				for(j=tmp.length()-1;j>=0;j--)
					if(tmp.charAt(j)!=' ')
						S.push(tmp.charAt(j));
				}
			}
		}
	System.out.println("Not accepted");
	}
}
