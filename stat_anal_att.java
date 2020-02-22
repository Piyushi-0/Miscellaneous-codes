/*
Java-program for Statistical Analyzer Attack
*/
import java.util.*;
class SA
{
static int freq(char s[],char c,int l,int h)
	{
	if(l>h)
		return 0;
	if(l==h)
		{
		if(s[l]==c)
			return 1;
		else
			return 0;
		}
	int mid=(l+h)/2;
	if(s[mid]==c)
		return mid-l+1+freq(s,c,mid+1,h);
	if(s[mid]>c)
		return freq(s,c,l,mid-1);
	return 0;
	}
public static void main(String args[])
	{
	Scanner sc=new Scanner(System.in);
	char s[]=new char[500];
	System.out.println("Enter plain text");
	String sr=sc.next();
	s=sr.toCharArray();
	int i,f=1,l=sr.length();
	Arrays.sort(s);
	//for(i=0;i<l;i++)
	//
	System.out.println(s[i]);
	char fr[][]=new char[l][2];
	int t=0,mf=0;
	for(i=0;i<l;i=i+f)
		{
		f=freq(s,s[i],i,l-1);
		//System.out.println("Frequency of "+s[i]+" is "+f);
		fr[t][0]=s[i];
		fr[t++][1]=(char)f;
		}
	for(i=0;i<t;i++)
		{
		if(i==0 || fr[i][1]>mf)
		mf=i;
		}
	System.out.println("Frequency of "+fr[mf][0]+" is max");
	int e=(fr[mf][0]-97+26-('e'-97))%26;
	System.out.println(e);
	System.out.println("Plain text derived\n");
	for(i=0;i<l;i++)
		{
		char c=(char)(((int)s[i]-97+e)%26+97);
		System.out.print(c);
		}
	System.out.println("");
	}
}
