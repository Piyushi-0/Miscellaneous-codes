/*
CLRS-15.6
DYNAMIC POGRAMMING QUESTION:
Professor Stewart is consulting for the president of a corporation that is planning
a company party. The company has a hierarchical structure; that is, the supervisor
relation forms a tree rooted at the president. The personnel office has ranked each
employee with a conviviality rating, which is a real number. In order to make the
party fun for all attendees, the president does not want both an employee and his
or her immediate supervisor to attend.
Professor Stewart is given the tree that describes the structure of the corporation,
using the left-child, right-sibling representation described in Section 10.4. Each
node of the tree holds, in addition to the pointers, the name of an employee and
that employee’s conviviality ranking. Describe an algorithm to make up a guest
list that maximizes the sum of the conviviality ratings of the guests. Analyze the
running time of your algorithm.
*/
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
struct node{
int cval, opv, g0c1;
char name[100];
struct node* l_ch;
struct node* r_sib;
struct node* par;
};
typedef struct node node;

node* getNode(node* p, int val, char name[])
{
//printf("Entering %d with %d\n",id,val);
node* new_node=malloc(sizeof(node));
new_node->par=p;
new_node->cval=val;
strcpy(new_node->name, name);
new_node->opv=-1000;//DENOTES NOT COMPUTED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
new_node->r_sib=NULL;
new_node->l_ch=NULL;
return new_node;
}

node* q[100];
int front=-1,rear=-1;

void enq(node* elem)
{
q[++rear]=elem;//assuming no overflow
if(front==-1)
	++front;
}

node* deq()
{
node* elem=q[front];
++front;	//HANDLE UNDERFLOW!!!!!
return elem;
}

int empty_q()
{
if(front>rear)
	return 1;
return 0;
}

void inorder(node* root)
{
if(root==NULL)
	return;
inorder(root->l_ch);
printf("%s ",root->name);
inorder(root->r_sib);
}

void preorder(node* root)
{
if(root==NULL)
	return;
printf("%s ",root->name);
preorder(root->l_ch);
preorder(root->r_sib);
}

int max(int a, int b)
{
if(a>b)	
	return a;
return b;
}

int maxc(node* root)
{
if(root==NULL)
	return 0;
	
if(root->opv!=-1000)//computed already
	return root->opv;

if(root->l_ch==NULL)//if leaf then it's optimal & included
	{
	root->opv=root->cval;
	//root->
	return root->opv;
	}

int child_sum=0, gchild_sum=0;
	
node* cur=root->l_ch;
node* cur2=NULL;
while(cur!=NULL)
	{//printf("Cur %d\n",cur->id);
	child_sum+=maxc(cur);
	if(cur->l_ch!=NULL)
		{
		cur2=cur->l_ch;//printf("Cur2 %d\n",cur2->id);
		while(cur2!=NULL)
			{
			gchild_sum+=maxc(cur2);
			cur2=cur2->r_sib;
			}
		}
	cur=cur->r_sib;
	}

//printf("For id %d, child sum %d, gchild sum %d\n",root->id,child_sum,gchild_sum);
if(child_sum< root->cval+gchild_sum)
	{
	root->opv=root->cval+gchild_sum;	
	root->g0c1=0;
//	root->inc=1;
	}
else	
	{
	root->opv=child_sum;	//printf("selecting child with sum %d\n",child_sum);//& not including root
	root->g0c1=1;
	}//check inclusion!!!!!!!!!!!!
return root->opv;
}

void list(node* root)
{
if(root==NULL)	
	return;
if(root->l_ch==NULL)//If we come at a leaf then simply to display this & return
	{
	printf("%s\n",root->name);
	return;
	}
node* cur=NULL;
node* cur2=NULL;
if(root->g0c1==0)//gchild+root gives optimal
	{
	printf("%s\n",root->name);
//	gc
	if(root->l_ch!=NULL)
		cur=root->l_ch;
	while(cur!=NULL)
		{
		if(cur->l_ch!=NULL)
			cur2=cur->l_ch;
		while(cur2!=NULL)//all these are gchildren
			{//printf("Called for %s\n",cur2->name);
			list(cur2);//printf("Called for %s\n",cur2->name);
			cur2=cur2->r_sib;
			}
		cur=cur->r_sib;
		}
	}
else
	{
//	list(); child
	if(root->l_ch!=NULL)
		cur=root->l_ch;
	while(cur!=NULL)
		{
		list(cur);
		cur=cur->r_sib;
		}
	}
}

void main()
{
node *root;
int r, i, id;
char name[100];
printf("Enter name, rating of President\n");
scanf("%s%d",name,&r);
root=getNode(NULL, r, name);
enq(root);

while(!empty_q())
	{//printf("Equals root?%d\n",cur==root);
	node* cur=deq();//cur is going to be parent for this iteration	
	int nch=0;
	printf("Enter no. of children of %s\n",cur->name);
	scanf("%d",&nch);	
	for(i=0;i<nch;i++)
		{
		printf("Enter name, rating of children\n");
		scanf("%s",name);
		scanf("%d",&r);
		//printf("Sending %d with %d\n",id, r);
		node* new_node=getNode(cur, r, name);//Next iteration of inner while tells its r_sib, next iteration of outer while tells its l_ch
		enq(new_node);
		if(cur->l_ch==NULL)
			cur->l_ch=new_node;
		else//put next to the last child
			{
			node* temp=cur->l_ch;
			while(temp->r_sib!=NULL)
				temp=temp->r_sib;
			temp->r_sib=new_node;
			}
		}
	}
/*	
printf("Printing inorder\n");
inorder(root);
printf("\n");
printf("Printing preorder\n");
preorder(root);
printf("\n");
*/
printf("Max possible sum achieved=%d\n",maxc(root));
printf("Guest list for the party:\n");
list(root);
}

