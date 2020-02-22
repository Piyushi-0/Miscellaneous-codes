/*
C program for Vigenere-Cipher
*/
#include<stdio.h>
int findRow(char c) 
	{
	int i;
	for (i=0;i<26;i++) 
		{
		if(arr[0][i]==c)
		return(i);
		}
	}
int findColumn(char c) 
	{
	int i;
	for (i=0;i<26;i++) 
		{
		if(arr[i][0]==c)
		return(i);
		}
	}
int findDecRow(char c,int j) 
	{
	int i;
	for (i=0;i<26;i++) 
		{
		if(arr[i][j]==c)
		return(i);
		}
	}

char arr[26][26];
char msg[22],key[22],emsg[22],retmsg[22];
void main()
{
int i=0,j,k,r,c;
k=96;
for (i=0;i<26;i++) 
	{
	k++;
	for (j=0;j<26;j++) 
		{
		arr[i][j]=k++;
		if(k==123)
		k=97;
		}
	}
printf("\nEnter msg\n");
gets(msg);
printf("\nEnter the key\n");
gets(key);
// Encryption
for (i=0;key[i]!=NULL;i++) 
	{
	c=findRow(key[i]);
	r=findColumn(msg[i]);
	emsg[i]=arr[r][c];
	}
emsg[i]='\0';
printf("\n Encrypted msg is:%s\n\n",emsg);
//decryption
for (i=0;key[i]!=NULL;i++) 
	{
	c=findColumn(key[i]);
	r=findDecRow(emsg[i],c);
	retmsg[i]=arr[r][0];
	}
retmsg[i]='\0';
printf("\n\nmsg Retrieved is:%s\n\n",retmsg);
}

