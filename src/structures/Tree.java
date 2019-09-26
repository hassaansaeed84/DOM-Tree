package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	private boolean isAngleBracket(String s) {
		if(s.charAt(0)=='<') {
			return true;
		}
		return false;
	}
	private boolean isAngleBracket(TagNode x) {
		if(x.tag.equals("html") || x.tag.equals("body") || x.tag.equals("p") || x.tag.equals("em") || x.tag.equals("b") || 
		   x.tag.equals("ol") || x.tag.equals("ul") || x.tag.equals("li") || x.tag.equals("table") || x.tag.equals("tr")||
		   x.tag.equals("td"))
		{
			return true;
		}
		return false;
	}
	private String AngleBracketTag(String s) {
		return s.substring(1, s.indexOf(">"));
	}
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		TagNode ptr=null;
		TagNode t=null;
		Stack<TagNode> tags= new Stack<TagNode>();
		String l=sc.nextLine();
		root= new TagNode(AngleBracketTag(l), null,null);
		tags.push(root);
		while (sc.hasNextLine()==true)
		{
			String n=sc.nextLine();
			if(isAngleBracket(n)==true)
			{
				t=new TagNode(AngleBracketTag(n), null, null);
			}
			else
			{
				t=new TagNode(n, null, null);
			}
			if(isAngleBracket(n)==true && n.charAt(1)=='/') 
			{
				tags.pop();
				continue;
			}
			if(n.contains("/")==false)
			{
				if(tags.peek().firstChild==null)
				{
					tags.peek().firstChild=t;
				}
				else
				{
					if(tags.peek().firstChild.sibling==null) {
						tags.peek().firstChild.sibling=t;
					}
					else
					{
						ptr = tags.peek().firstChild.sibling;
						ptr.sibling=t;
					}
				}
				if(isAngleBracket(n)==true)
				{
					tags.push(t);
				}
			}
		}
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		// Check Ending. It dont search through whole tree
		//FINISHED
		Stack<TagNode> p = new Stack<TagNode>();
		TagNode ptr=root;
		while(ptr!=null)
			{
			if(ptr.tag.equals(oldTag)) 
			{
				ptr.tag=newTag;
			}
			if(ptr.firstChild!=null) {
				if(ptr.sibling!=null)
				{
					p.push(ptr.sibling);
				}
				ptr=ptr.firstChild;
			}
			else if(ptr.sibling!=null) {
				ptr=ptr.sibling;
			}
			else if(p.isEmpty()==false && ptr!=p.peek() )
			{
				ptr=p.pop();
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		int rowMatch=0;
		Stack<TagNode> bold= new Stack<TagNode>();
		Stack<TagNode> p = new Stack<TagNode>();
		TagNode ptr=root;
		while(ptr!=null)
			{
			if(ptr.tag.equals("tr")) 
			{
				rowMatch++;
			}
			if(rowMatch==row)
			{
				break;
			}
			if(ptr.firstChild!=null) {
				if(ptr.sibling!=null)
				{
					p.push(ptr.sibling);
				}
				ptr=ptr.firstChild;
			}
			else if(ptr.sibling!=null) {
				ptr=ptr.sibling;
			}
			else if(p.isEmpty()==false && ptr!=p.peek() )
			{
				ptr=p.pop();
			}
			else
			{
				break;
			}
		}
		TagNode B=ptr.firstChild;
		while(B!=null) 
		{
			if(B.sibling!=null) 
			{
				bold.push(B.sibling);
			}
			if(B.tag.equals("td")) 
			{
				TagNode temp=B.firstChild;
				B.firstChild=new TagNode("b", null, null);
				B.firstChild.firstChild=temp;
			}
			if(bold.isEmpty()==false && B!=bold.peek()) 
			{
				B=bold.pop();
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		Stack<TagNode> p = new Stack<TagNode>();
		Stack<TagNode> x = new Stack<TagNode>();
		TagNode prev=null;
		TagNode ptr=root;
		while(ptr!=null)
			{
			if(ptr.tag.equals(tag)) 
			{
				if(tag.equals("ol")|| tag.equals("ul"))
				{
					TagNode ptr2=ptr.firstChild;
					while(ptr2!=null) 
					{
						if(ptr2.tag.equals("li"))
						{
							ptr2.tag="p";
						}
						ptr2=ptr2.sibling;
					}
				}
				if(prev.firstChild==ptr)
				{
					TagNode temp=ptr.sibling;
					prev.firstChild=ptr.firstChild;
					TagNode ptr3=prev.firstChild;
					while(ptr3.sibling!=null) {
						ptr3=ptr3.sibling;
					}
					ptr3.sibling=temp;
				}
				else if(prev.sibling==ptr)
				{
					TagNode temp=ptr.sibling;
					prev.sibling=ptr.firstChild;
					TagNode ptr4=prev.sibling;
					while(ptr4.sibling!=null)
					{
						ptr4=ptr4.sibling;
					}
					ptr4.sibling=temp;
				}
			}
			if(ptr.firstChild!=null) {
				if(ptr.sibling!=null)
				{
					if(ptr.tag.equals(tag)) {
						x.push(ptr.firstChild);
					}
					else {
						x.push(ptr);
					}
					p.push(ptr.sibling);
				}
				prev=ptr;
				ptr=ptr.firstChild;
				
			}
			else if(ptr.sibling!=null) {
					prev=ptr;
					ptr=ptr.sibling;				
			}
			else if(p.isEmpty()==false && x.isEmpty()==false)
			{
				ptr=p.pop();
				prev=x.pop();
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		//Testing ex2.html with "an" and then "item"
		Stack<TagNode> p = new Stack<TagNode>();
		Stack<TagNode> X= new Stack<TagNode>();
		TagNode prev=null;
		TagNode ptr=root;
		while(ptr!=null)
			{
			if(isAngleBracket(ptr)==false) {
				String x=ptr.tag.toLowerCase();
				String Word=word.toLowerCase();
				
				if(x.contains(Word)) 
				{
					TagNode temp=null;
					if(ptr.sibling!=null) {
						temp=ptr.sibling;
					}
					if(x.equals(Word) || x.equals(Word.concat("?")) || x.equals(Word.concat("!")) || 
					   x.equals(Word.concat(".")) || x.equals(Word.concat(";")) || x.equals(Word.concat(":")))
					{
						if(prev.firstChild==ptr) {
							TagNode item=new TagNode(ptr.tag, null, null);
							prev.firstChild=new TagNode(tag,null, temp);
							prev.firstChild.firstChild=item;
						}
						else if(prev.sibling==ptr) {
							TagNode item=new TagNode(ptr.tag, null, null);
							prev.sibling=new TagNode(tag, null, temp);
							prev.sibling.firstChild=item;
						}
					}
					else 
					{
						String []s=x.split(" ");
						String []split=ptr.tag.split(" ");
						for(int i=0; i<s.length;i++) 
						{
							if(s[i].equals(Word) || s[i].equals(Word.concat("?")) || s[i].equals(Word.concat("!")) || 
							   s[i].equals(Word.concat(".")) || s[i].equals(Word.concat(";")) || s[i].equals(Word.concat(":"))) 
							{	
								if(i==0)
								{
									prev.firstChild=new TagNode(tag,null, null);
									prev.firstChild.firstChild=new TagNode(split[i], null ,null);
									int j=i+1;
									String S=" ";
									while(j<s.length)
									{
										if(j==s.length-1) {
											S+=split[j];
										}
										else {
											S+=split[j].concat(" ");
										}
										j++;
									}
									prev.firstChild.sibling=new TagNode(S, null, temp);
									
								}
								else if(i<s.length-1 && i>0)
								{
									String o=" ";
									o+=split[i].concat(" ");
									String[] z= ptr.tag.split(o);
									ptr.tag= z[0].concat(" ");
									TagNode ptr3=ptr;
									for(int k=0;k<z.length;k++) 
									{
										if(k!=z.length-1)
										{
											TagNode extra=new TagNode(split[i], null,null);
											ptr3.sibling=new TagNode(tag,extra,null);
											ptr3=ptr3.sibling;
										}
										else 
										{
											String v=" ";
											v+=z[k];
											ptr3.sibling=new TagNode(v,null,null);
											ptr3=ptr3.sibling;
										}
									}
									if(temp!=null) {
										ptr3.sibling=temp;
									}
								}
								else
								{
									ptr.tag=ptr.tag.replace(split[i], "");
									TagNode extra=new TagNode(split[i], null,null);
									TagNode extratag=new TagNode(tag, extra,temp);
									ptr.sibling=extratag;
								}
							}
						}
					}
					if(temp!=null)
					{
						ptr=temp;
					}
					else if(p.isEmpty()==false && X.isEmpty()==false) 
					{
						prev=X.pop();
						ptr=p.pop();
					}
					else 
					{
						ptr=null;
					}
					continue;
				}
			}
			if(ptr.firstChild!=null) {
				if(ptr.sibling!=null)
				{
					X.push(ptr);
					p.push(ptr.sibling);
				}
				prev=ptr;
				ptr=ptr.firstChild;
			}
			else if(ptr.sibling!=null) {
				prev=ptr;
				ptr=ptr.sibling;
			}
			else if(p.isEmpty()==false && X.isEmpty()==false)
			{
				prev=X.pop();
				ptr=p.pop();
			}
			else
			{
				break;
			}
		}
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
