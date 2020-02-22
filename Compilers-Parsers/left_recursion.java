/*
Program to remove left recursion in Java. Map data structure is used with LHS as key and RHS as it's value. Since new productions are added while removing left recursion, ConcurrentHashMap is used. In a production 
P->Pa|b where a and b don't start with P,
alpha and beta are separately retrieved and stored. All the starting variable/terminal after "|" is also checked to find if the production is left recursive. Then according to rule, P->bQ and Q->aQ|e are added removing the left-recursing production.
*/
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
class G
{
public static void main(String args[])
	{
	Map <String, String> hm=new ConcurrentHashMap<String,String>();
	hm.put("E","E+T|T");
	hm.put("T","T*F|F");
	hm.put("F","(E)|i");
	int x,x1,i;
	char c='A';
	String sv, sk;
	StringBuilder alpha=new StringBuilder(""), beta=new StringBuilder(""), s1;
	Iterator<String> it = hm.keySet().iterator();
	while(it.hasNext())
		{		
		x=x1=-1;
		alpha=new StringBuilder("");
		beta=new StringBuilder("");
		sk=it.next();
		sv=hm.get(sk);
		System.out.println(sk+" "+sv);
		s1=new StringBuilder("");
		do
			{
			if(sv.charAt(x+1)==sk.charAt(0))//left recursion
				{		
				for(String s:sv.split("[|]"))
					{
					if(s.charAt(0)==sk.charAt(0))
						alpha.append(s.substring(1,s.length())+" ");
					else
						beta.append(s+" ");
					}
				for(String s:beta.toString().split(" "))//beta1A|beta2A...
					s1.append(s+c+"|");
				s1.deleteCharAt(s1.length()-1);
				hm.put(sk, s1.toString());//replaced

				s1=new StringBuilder("");
				for(String s:alpha.toString().split(" "))
					s1.append(s+c+"|");//alpha1A|alpha2A...
				s1.append("e");//|e
				hm.put(c+"",s1.toString());
				}
			x=sv.indexOf("|",x1+1);
			x1=x;
			}while(x>=0);
		c++;
		}		
	System.out.println(hm);
	}
}
