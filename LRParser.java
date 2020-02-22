/*
Code for LRP parser
*/

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
class LRP
{
int match(String s1, String s)
	{
	s=s.replace(".","");
	int i=1;
	for(String st:s.split(" "))
		{//System.out.println("st="+st+"s="+s1);
		if(st.equals(s1))
			break;
		i++;
		}
	return i;
	}
public static void main(String args[])
	{
	Map<String, String> hm=new ConcurrentHashMap<String, String>();
	StringBuilder p=new StringBuilder("B.A A.a");
	String s,pr;
	char ch;
	int c=0, d, n=0;
	hm.put("I"+c,p.toString());
	//table with terminals and non-terminals
	Map<String, String> ht=new HashMap<String, String>();
	Iterator<String> it=hm.keySet().iterator();
	Stack<String> st=new Stack<String>();
	st.push("I0");
	//while(it.hasNext())
	while(!st.empty())
		{
		s=st.pop();//System.out.println(s);
		pr=hm.get(s);//System.out.println(pr);
	//	n=hm.keySet().size();
		for(String r:pr.split(" "))//!!!!!!!!!!!!!!!!!!!DIFFERENT STATE ONLY IF DIFFERENT NEXT SYMBOL
			{
			String r1=r.substring(1);//EXTRACTED RHS OF PRODUCTION OF THIS STATE
			d=r1.indexOf(".");
			if(d+1<r1.length())//not Final item or Accept state
				{
				//shift for this character
				ch=r1.charAt(d+1);
				c++;//NEXT STATE NUMBER
				ht.put(s+""+ch,"S"+c);//PLACE IN TABLE//Here taking same for non-terminal also
				//
				//shifting .
				p=new StringBuilder(r);//SINCE d is INDEX OF r1
				p.deleteCharAt(d+1);
				p.insert(d+1+1,".");
				//
				//putting in new state
				st.push("I"+c);
				hm.put("I"+c,p.toString());//System.out.println(hm.keySet());
				//
				}
			else//FINAL ITEM OR ACCEPT STATE
				{
				r=r.replace(".","");//System.out.println(r);
				int i=new LRP().match(r,hm.get("I0"));//PRODUCTION NO.
				if(i!=1)//FINAL ITEM
					{
					ht.put(s+"a","r"+i);
					ht.put(s+"$","r"+i);
					}
				else//ACCEPT STATE
					ht.put(s+"$","a");
				}
			}
		}
	System.out.println(ht);
//	System.out.println(hm);//PRINT PROPERLY
	}
}
