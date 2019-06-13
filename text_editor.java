import java.util.*;
class D{
public static void main(String args[])
	{
	Scanner sc=new Scanner(System.in);
	String inp;
	inp="asdf#q#pqr^^23";//sc.nextLine();
	List<StringBuilder> l=new LinkedList<StringBuilder>();
	StringBuilder s=new StringBuilder("");
	l.add(s);
	int i, len=inp.length(), caps=0, x=0, j=0, cc=0;
	char c;
	for(i=0;i<len;i++)
		{//we use cc as index at which insertion
		c=inp.charAt(i);
		if(Character.isLetterOrDigit(c)||(c==' '))
			{
			if(caps==1 && Character.isLetter(c))
				c=Character.toUpperCase(c);
			if(cc<=l.get(x).length())
				l.get(x).insert(cc,c);
			else
				l.get(x).append(c);//OR l.get(x).insert(l.get(x).length(), c);
			cc++;
			}	

		else if(c=='<')
		{
			if(cc==0)
				{
				if(x>0)
					{
					x--;//current decremented
					cc=l.get(x).length();//if not enough chars in this line current col to the end
					}
				}
			else
			cc--;
		}
		
		else if(c=='>')
		{
			if(cc==l.get(x).length())
			{	
			if(x<j)	
				{
				x++;//current row changed
				cc=0;//to start of next line
				}
			}
			else cc++;	
		}
		
		else if(c=='@')
			caps=1-caps;
		else if(c=='?' && x<j)//!!!!!!!!!IF KEY PRESSED CONTINUOUSLY
			{
			x++;//current row incremented
			if(i==len-1|| (inp.charAt(i+1)!=c && l.get(x).length()<cc))//=>only if c is the last of its kind
					cc=l.get(x).length();			//if not enough chars in this line current col to the end
			}
		else if(c=='^' && x>0)//!!!!!!!!!IF KEY PRESSED CONTINUOUSLY
			{
			x--;//current row decremented
			if(i==len-1 || (inp.charAt(i+1)!=c && l.get(x).length()<cc))//=>only if c is the last of its kind
					cc=l.get(x).length();			//if not enough chars in this line current col to the end
			}
		else if(c=='/')//backspace
			{
			if(cc>0)
				{
				l.get(x).deleteCharAt(cc-1);//remove previous character & shift current col left
				cc--;	
				}
			else if(cc==0 && x>0)
				{
				cc=l.get(x-1).length();//current line merged with previous line & current column to the end of original previous line
				l.get(x-1).append(l.get(x));
				l.remove(x);//remove current line
				x--;//current row decremented
				j--;//total rows decremented
				}
			}
		else if(c=='#')
			{
			j++;//add new line
			StringBuilder s1=new StringBuilder("");
			l.add(s1);
			cc=0;x++;//current column 0, current row incremented
			}
		}
	
	for(i=0;i<l.size();i++)
		System.out.println(l.get(i));
	}
}