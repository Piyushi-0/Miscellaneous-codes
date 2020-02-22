/*
Program to find first and follow in given grammar. There is an array corresponding to every Non-terminal for storing it's first and follow since this will be used further in computing first or follow of some other variable. First and follow are recursively computed according to the rules:
first of terminal is terminal, of variable is the first non-terminal.
if variable also derives epsilon(e) and there is another variable following it then first is according to first of next variable and so on.
For follow:
for start symbol is $ is always there.
for all variables where it appears on RHS if that is the last then follow is of LHS else follow is first of next.
If first of next contains 'e' then follow of next is also included and so on.
*/
import java.util.*;	
class FF			
{
boolean terminal(char ch)
	{
	if(!Character.isLetter(ch) || (Character.isLowerCase(ch)) )
		return true;
	return false;
	}
StringBuilder first(char c, String p[][], StringBuilder f[])//FIRST OF EPSILON
	{
	FF o=new FF();
	int i, x=-1, x1=-1;
	for(i=0;i<4;i++)
		{
		if(p[i][0].charAt(0)==c)
			{
			if(!f[i].toString().equals(""))
				return f[i];
			do
				{
				if(o.terminal(p[i][1].charAt(x+1)))
					f[i].append(p[i][1].charAt(x+1));
				else//NON-TERMINAL
					{
					StringBuilder s=new StringBuilder(first(p[i][1].charAt(x+1), p, f));
					int d=x+1, t=s.indexOf("e");
					while(t>=0 && d<p[i][1].length()-1)
						{
						s.deleteCharAt(t);
						s.append(first(p[i][1].charAt(d+1), p, f));
						t=s.indexOf("e");
						d++;
						}
					f[i].append(s);
					}
				x=p[i][1].indexOf("|",x1+1);//x1
				x1=x;
				}while(x>=0);
			return f[i];
			}
		}
	return f[i];
	}	
	
StringBuilder follow(char ch, String p[][], StringBuilder fl[])//FOLLOW OF EPSILON!!!!!
	{
	FF o=new FF();
	int i, d, x1=-1, j=0;
	char c;
	for(i=0;i<4;i++)//THIS COMES jTH IN THE PRODUCTIONS' SEQUENCE
		{
		if(p[i][0].charAt(0)==ch)//ALREADY CALCULATED	
			{
			j=i;
			if(!fl[j].toString().equals(""))
				return fl[j];
			}
		}
	if(ch==p[0][0].charAt(0))//KNOWING START SYMBOL'S POSITION
		fl[0].append("$");
	for(i=0;i<4;i++)
		{
		d=p[i][1].indexOf(ch+"",x1+1);//IN WHICHEVER PRODUCTIONS' RHS
		x1=d;//NEXT TIME AFTER THIS
		while(d>=0)
			{
			if(d+1<=p[i][1].length()-1)//DOESN'T COME AT THE END
				{
				c=p[i][1].charAt(d+1);//NEXT TO IT
				if(o.terminal(c))//OR HERE ALSO VIA FIRST
					{
					if(c!='e')
						fl[j].append(c+"");
					else//FIRST OF EPSILON IS FOLLOW OF LHS PRODUCTION
						{
						fl[j].append(o.follow(p[i][0].charAt(0) , p, fl));
						}
					}
				else//IF NOT TERMINAL
					{
					StringBuilder s=new StringBuilder(o.first(c, p, f));//FIRST OF NEXT
					int t=s.indexOf("e");
					int x=d+2;//AT D+1 IS PRODUCING NULL
					while(t>=0)//IF CONTAINS EPSILON
						{
						s.deleteCharAt(t);//REMOVE
						if(x==p[i][1].length() && p[i][0].charAt(0)!=ch)
							s.append(follow(p[i][0].charAt(0), p, fl));//UNION FOLLOW
						else
							{
							s.append(o.first(p[i][1].charAt(x), p, f));
							x++;						
							t=s.indexOf("e");
							}
						}
					fl[j].append(s);
					}
				}
			else
				{
				if(p[i][0].charAt(0)!=ch)//NOT TO BE IT'S OWN FOLLOW
					fl[j].append(follow(p[i][0].charAt(0), p, fl));
				}
			d=p[i][1].indexOf(ch+"",x1+1);//IN NEXT PLACE IN THE SAME PRODUCTION
			x1=d;
			}
		}
	return fl[j];
	}
static StringBuilder f[], fl[];
public static void main(String args[])	
	{
	FF o=new FF();
	String p[][]=new String[4][2];
	p[0][0]="S";p[0][1]="AB";
	p[1][0]="A";p[1][1]="e|aA";
	p[2][0]="B";p[2][1]="b|C";
	p[3][0]="C";p[3][1]="i";
	Scanner sc=new Scanner(System.in);
	fl=new StringBuilder[4];//first of 4 terminals
	f=new StringBuilder[4];
	int i;
	for(i=0;i<4;i++)
		{
		f[i]=new StringBuilder("");
		fl[i]=new StringBuilder("");
		}
	for(i=0;i<4;i++)
		{
		o.follow(p[i][0].charAt(0), p, fl);
		}
	for(i=0;i<4;i++)
		o.first(p[i][0].charAt(0),p,f);
	System.out.println("First of terminals in sequence as appearing in productions are:");
	for(i=0;i<4;i++)
		System.out.println(p[i][0].charAt(0)+" "+f[i]);
	System.out.println("Follow of terminals in sequence as appearing in productions are:");
	for(i=0;i<4;i++)
		System.out.println(p[i][0].charAt(0)+" "+fl[i]);
	}
}	
	
