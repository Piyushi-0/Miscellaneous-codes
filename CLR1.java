/*
Code for CLR1 parser
*/

import java.util.*;
class CLR1
{
static HashMap<String,String> hm=new HashMap <String,String>(), fs=new HashMap <String,String>(), fl=new HashMap <String,String>();
static char orig;
static boolean terminal(char ch)
	{
//	if(ch==' ')
		//return false;
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
	hm.put("A","Aa|a");//grammar

	int i;
	System.out.println("Production:\n"+hm);
	
	Map<String, String> hm1=new HashMap<String, String>();//key state no., value productions
	StringBuilder p=new StringBuilder("");
	p.append("B.A,$ ");
	
	for(String s:hm.get("A").split("[|]"))//RHS of A
		p.append("A."+s+","+",$ ");//p="B.A,$ A.Aa,$ A.a,$ "
	
	String la="$";//already put
	char nd=' ', next=' ';//next to dot
	
	for(String s:p.toString().split(" "))
		{
		if(s.charAt(s.indexOf(".")+1)!=',')
			{
			nd=s.charAt(s.indexOf(".")+1);
			}
		else
			continue;
		if(nd!=' ' && !terminal(nd))
			{
			next=(s.charAt(s.indexOf(".")+2)==',')?s.charAt(s.indexOf(".")+2):s.charAt(s.indexOf(".")+3);
			}
		else
			continue;

		for(String s2:p.toString().split(" "))//re-scan wherever this LHS add more la
			{
			if(s2.charAt(0)==s.charAt(0))
				{
				if(terminal(next) && la.indexOf(next)<0)//not epsilon
					p=p.insert(p.indexOf(s2)+s2.length(),next+"");
				else if(!terminal(next))
					{
					String f=first(next+"");
					for(String f2:f.split(" "))
						{
						p=p.insert(p.indexOf(s2)+s2.length(),f2);						
						}
					}
				}
			}
		}
		
		
		System.out.println(p);

	}
}
