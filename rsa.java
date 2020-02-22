/*Java-program for RSA protocol*/
import java.util.*;
class j
{
static int isPrime(int n)
	{
	int i;
	if(n==1)
		return 0;
	for(i=2;i<=Math.sqrt(n);i++)
		if(n%i==0)
			return 0;
	return 1;
	}
static int randPrime()
{
	int i=(int)(Math.random()*100);
	while(isPrime(i)==0)
		{
		i=(int)(Math.random()*100);
		}
	return i;
}
static int fastExpo(int a,int b,int n)
{
StringBuilder bin=new StringBuilder("");
while(b>0)
	{
	if(b%2==0)
	bin.append("0");
	else bin.append("1");
	b=b/2;
	}
bin=bin.reverse();
System.out.println(bin);
int i,l=bin.length(),f=1;
for(i=0;i<l;i++)
	{
	f=(f*f)%n;
	if(bin.charAt(i)=='1')
		f=(f*a)%n;
	System.out.println(f);
	}
return f;
}
static int gcd(int a,int b)
	{
	int i=a;
	while(i%a!=0 && i%b!=0)
		i++;
	return i;
	}
static int modInv(int e,int phi)
	{
	int i=1;
	while(i*e%phi!=1)
		i++;
	return i;
	}
	
public static void main(String args[])
	{
	Scanner sc=new Scanner(System.in);
	int p=randPrime(),q=randPrime();
	while(q==p)
	q=randPrime();
	int i,phi=(p-1)*(q-1);
	for(i=2;i<phi;i++)
		{
		if(gcd(phi,i)==1)
			{
			e=i;
			break;
			}
		}
	int d=modInv(e,phi);
	System.out.println("Enter message\n");
	int m=sc.nextInt();
	System.out.println("Enter mod\n");
	int n=sc.nextInt();
	System.out.println("Encrypted "+fastExpo(m,e,n));
	System.out.println("Decrypted "+fastExpo(c,d,n));
	}
}
