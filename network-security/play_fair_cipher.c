/*
C program for PlayFair-Cipher
*/
#include<stdio.h>
#include<string.h>
char km[5][5];
int getrow(char c)
{
int i,j;
for(i=0;i<5;i++)
for(j=0;j<5;j++)
if(km[i][j]==c)
return i;
}
int getcol(char c)
{
int i,j;
for(i=0;i<5;i++)
for(j=0;j<5;j++)
if(km[i][j]==c)
return j;
}
char character(int k)
{
char c=97+k;
return c;
}
int present(int c)
{
int i,j;
for(i=0;i<5;i++)
	for(j=0;j<5;j++)
		if(km[i][j]==c)
			return 1;
return 0;
}

void main()
{
char key[25];
int i,j;
printf("Enter key of max length 25\n");
scanf("%s",key);
int l=strlen(key);
int k=0;
int t;
for(i=0;i<5;i++)
	for(j=0;j<5;j++)
		km[i][j]=' ';
i=0;
int flag;
while(1)
	{
	if(key[k]=='\0')
	break;
	for(j=0;j<5;j++)
		{
		flag=1;
		for(t=0;t<k;t++)
			{
			if(key[t]==key[k])
				{
				flag=0;
				break;
				}
			}
		if(flag==0)
			km[i][j]=key[++k];
		else
			km[i][j]=key[k];
		k++;
		if(key[k]=='\0')
			break;
		}
	i++;
	}
for(i=0;i<5;i++)
	{
	for(j=0;j<5;j++)
		printf("%c ",km[i][j]);
	printf("\n");
	}
k=0;
char c;

for(i=0;i<5;i++)
	{
	for(j=0;j<5;j++)
		{
		if(km[i][j]==' ')
			{
			c=character(k++);
			if(c=='j')
			c=character(k++);
			while(present(c))
				{
				c=character(k++);
				}
			km[i][j]=c;
			}
		}
	}
for(i=0;i<5;i++)
	{
	for(j=0;j<5;j++)
		printf("%c ",km[i][j]);
	printf("\n");
	}
char plain[100];
printf("Enter plain text\n");
scanf("%s",plain);
int len=strlen(plain);
for(i=0;i<len-1;i++)
	{
	if(plain[i]==plain[i+1])
		{
		int ins=i+1;
		for(j=len;j>=ins;j--)
			{
			plain[j+1]=plain[j];
			}
		plain[ins]='z';
		}
	}
len=strlen(plain);
if(len%2!=0)
	{
	plain[len+1]='z';
	plain[len+2]='\0';
	}
len=strlen(plain);
//printf("%s\n",plain);
int col1,i1=1,col2,row1,row2;
k=0;i=0;
while(i1<len)
	{
	col1=getcol(plain[i]);
	col2=getcol(plain[i1]);
	row1=getrow(plain[i]);
	row2=getrow(plain[i1]);
	if(col1==col2)
		{
		plain[i]=km[(row1+1)%5][col1];
		plain[i1]=km[(row2+1)%5][col1];
		}
	else if(row1==row2)
		{
		plain[i]=km[row1][(col1+1)%5];
		plain[i1]=km[row1][(col2+1)%5];
		}
	else
		{
		t=col1;
		j=row2;
		while(t!=j)
			{
			t=(t+1)%5;
			j=(j-1)%5;
			}
		plain[i]=km[j][t];
		t=row1;
		j=col2;
		while(t!=j)
			{
			t=(t+1)%5;
			j=(j-1)%5;
			plain[i1]=km[t][j];
			}
		}
	i+=2;
	i1+=2;
	}
printf("Cipher text is %s\n",plain);
}

