/*
Code for SLR1 parser
*/

import java.util.*;
class SLR1
{
static HashMap<String,String> hm=new HashMap <String,String>(), fs=new HashMap <String,String>(), fl=new HashMap <String,String>();
static char orig;
static boolean terminal(char ch)
	{
	if(!Character.isLetter(ch)||Character.isLowerCase(ch))
		return true;
	return false;
	}
	
static String first(String key)
	{
	if(fs.get(key).length()>0) //already computed
		return fs.get(key);
	String p=hm.get(key); //production of this non-terminal
	String t="";
	for(String s:p.split("[|]"))
		{
		if(terminal(s.charAt(0))) //terminal is in first
			{
			t+=s.charAt(0)+" ";
			}
		else//non-terminal then proceed:
			{
			int x, d=-1;
			StringBuilder tmp=new StringBuilder(first(s.charAt(++d)+"")); //first of that Non-terminal
			x=tmp.indexOf("e "); //if contains epsilon then proceed:
			while(x>=0 && d<s.length()-1) //if charAt(s.length()-1) i.e. last of the production has epsilon then keep it
				{
				tmp.delete(x,x+2);
				tmp.append(first(s.charAt(++d)+""));
				x=tmp.indexOf("e ");
				}
			t+=tmp;//final first list in t
			}
		}
	fs.put(key,t);
	return t;
	}
static String follow(String key)//check LOOP AVOIDANCE 
	{
	if(fl.get(key).length()>0) //already computed
		return fl.get(key);
	String p, t="";
	if(key.charAt(0)=='A') //starting symbol
		t+="$ ";
	for(String k:hm.keySet())
		{
		p=hm.get(k);
		int x=p.indexOf(key); //index of terminal in ALL productions
		if(x>=0)
			{
			if(x+1<p.length() && p.charAt(x+1)!='|') //this is not last
				{
				char ch=p.charAt(x+1);
				if(terminal(ch))//next is terminal
					t+=ch+" ";//our first not for direct terminals won't be epsilon
				else//next is non-terminal
					{//epsilon could come here
					StringBuilder tmp=new StringBuilder(first(ch+""));// it's first 
					int i=tmp.indexOf("e "), d=x+1;
					while(i>=0 && d+1<p.length() && p.charAt(d+1)!='|') //first includes e
						{
						tmp.deleteCharAt(i);
						tmp.append(first(p.charAt(++d)+""));
						i=tmp.indexOf("e ");
						}
					if(i>=0)//then follows LHS non-terminal
						tmp.append(follow(k));
					t+=tmp;
					}
				}
			else if(k.charAt(0)!=orig)//key.charAt(0))//Then follows it's LHS
				t+=follow(k);
			}
		}
	fl.put(key, t);	
	return t;
	}	

int match(String s1, String s)
	{
	s=s.replace(".","");
	int i=1;
	for(String st:s.split(" "))
		{
		if(st.equals(s1))
			break;
		i++;
		}
	return i;
	}
public static void main(String args[])
	{fl.put("A","");fs.put("A","");//for first-follow
	hm.put("A","a");//grammar
	
	System.out.println("Production:\n"+hm);
	
	Map<String, String> hm1=new HashMap<String, String>();//key state no., value productions
	StringBuilder p=new StringBuilder("");
	p.append("B.A ");
	Iterator<String> it=hm.keySet().iterator();
	while(it.hasNext())
		{
		String key=it.next();
		p.append(key+"."+hm.get(key)+" ");//when RHS not '|' separated
		}
	String s,pr;
	char ch;
	int c=0, d, n=0, sn;
	hm1.put("I"+c,p.toString());
	//table with terminals and non-terminals
	Map<String, String> ht=new HashMap<String, String>();
	Stack<String> st=new Stack<String>();
	st.push("I0");
	while(!st.empty())
		{
		s=st.pop();
		pr=hm1.get(s);
		for(String r:pr.split(" "))//1 by 1 productions of the state
			{
			String r1=r.substring(1);//EXTRACTED RHS OF PRODUCTION OF THIS STATE
			d=r1.indexOf(".");
			if(d+1<r1.length())//not Final item or Accept state
				{
				//shift for this character
				ch=r1.charAt(d+1); //!!!!!!!!!!!!!ALL production of this state with this ch to be in same state.
				
				//shifting .
				p=new StringBuilder(r);//SINCE d is INDEX OF r1
				p.deleteCharAt(d+1);
				p.insert(d+1+1,".");
				//
				
				if(ht.containsKey(s+ch))//this character has only occured in this state & assigned a new state
				{//no need of new state
				if(ht.get(s+ch).charAt(0)=='r')
					{
					System.out.println("Conflict!!!");
					break;
					}
				sn=Integer.parseInt(ht.get(s+ch).substring(1));
				hm1.put("I"+sn ,hm1.get("I"+sn)+p);
				}
				
				else{
				c++;//NEXT STATE NUMBER
				ht.put(s+""+ch,"S"+c);//PLACE IN TABLE//Here taking same for non-terminal also
				//
				}
				
				//putting in new state
				st.push("I"+c);
				hm1.put("I"+c,p.toString());//System.out.println(hm1.keySet());
				//
				}
			else//FINAL ITEM OR ACCEPT STATE
				{
				r=r.replace(".","");//System.out.println(r);
				int i=new LRP().match(r,hm1.get("I0"));//PRODUCTION NO.
				if(i==1)//Accept State
					ht.put(s+"$","a");
				else//FINAL ITEM
					{
					char c1=r.charAt(0);
					
					orig=c1;
					
					String fr=follow(c1+"");
					for (String f1:fr.split(" "))
						ht.put(s+f1,"r"+i);
//					ht.put(s+"a","r"+i);
//					ht.put(s+"$","r"+i);
					}
				}
			}
		}
	System.out.println("Parsing Table:");
	System.out.println(ht);
//	System.out.println(hm1);//PRINT PROPERLY
	}
}
