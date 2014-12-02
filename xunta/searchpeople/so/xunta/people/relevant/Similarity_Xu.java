package so.xunta.people.relevant; 

import org.apache.lucene.index.FieldInvertState;
//import org.apache.lucene.index.Norm;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;

//2013.6.10:创建自己的计分方式.
//2013.6.20: 修改了与长度有关的打分方法,在创建索引时会发挥作用,值最终体现在索引中的fieldNorm里.
//这个在xunta_43里.

public class Similarity_Xu extends DefaultSimilarity {
//public class Similarity_Xu extends TFIDFSimilarity {
	
	// @Override
	 public float lengthNorm(FieldInvertState state) {
	    int numTerms;//原来采用的量值.
		 float length_xu=0;//length_xu=(offset+length)/2;//offset:最后一个词的偏移量. length:所有单词的总和数.
	    
	    //System.out.println("xunta43: "+numTerms);
	    if (discountOverlaps){
	      //numTerms = state.getLength() - state.getNumOverlap();
	      System.out.println("-------------------getName: "+state.getName());
	      System.out.println("getNumOverlap: "+state.getNumOverlap());
	      	      System.out.println("getMaxTermFrequency: "+state.getMaxTermFrequency());
	      System.out.println("getPosition: "+state.getPosition());
	      System.out.println("getUniqueTermCount: "+state.getUniqueTermCount());
	      System.out.println("getOffset: "+state.getOffset());
	      System.out.println("xunta43-state.getLength(): "+state.getLength());
	      length_xu=(state.getLength()+state.getOffset()+state.getOffset())/(float)3;//length_xu=(offset+length)/2;//offset:最后一个词的偏移量. length:所有单词的总和数.
	    }else{
	      //numTerms = state.getLength();
	      //System.out.println("xunta43: "+numTerms);
	    	length_xu=(state.getLength()+state.getOffset()+state.getOffset())/(float)3;
		      
	    }
	    length_xu=(length_xu-50);//让长度在50时为最大权.
	    if (length_xu<0) length_xu=-length_xu;//取绝对值.
	    if (length_xu<5) length_xu=length_xu-(int)length_xu+5;//为避免length_xu-50=0,在小于5时,强迫为5.后面的小数保留原样,以保持文档的差异性.
	    System.out.println("length_xu: "+length_xu);
	    return state.getBoost() * ((float) (1.0 / Math.sqrt(length_xu)));
	   // return state.getBoost() * ((float) (1.0 / numTerms));//原来的算法.

	 }
	 //@Override
//	 long computeNorm(String field, FieldInvertState state) {
//		    int length = state.getOffset() - state.getPosition();
//		    System.out.println("numTerms5: ");
//		    return (long) (1.0 / (float)length);
//		}
//	 @override
//	public
//	 long computeNorm(FieldInvertState state) {
//		    int length = state.getOffset() - state.getPosition();
//		    System.out.println("numTerms6: ");
//		    return (long) (1.0 / (float)length);
//		}
	 
//	 @override
//	 public void computeNorm(FieldInvertState state,Norm norm) {
//		    int length = state.getOffset() - state.getPosition();
//		    System.out.println("numTerms6: ");
//		    return (1.0 / (float)length);
//		}
	 
	 
	 /** Implemented as <code>sqrt(freq)</code>. */
	 // @Override
	  public float tf(float freq) {
		    System.out.println("xunta43 1: tf freq:"+freq);
//return 1;
	    return (float)Math.sqrt(freq);
	    
	  }
	    
	  /** Implemented as <code>1 / (distance + 1)</code>. */
	//  @Override
	  public float sloppyFreq(int distance) {
		    System.out.println("xunta43 2: ");

	    return 1.0f / (distance + 1);
	  }
	  
	  /** Implemented as <code>log(numDocs/(docFreq+1)) + 1</code>. */
	//  @Override
	  public float idf(long docFreq, long numDocs) {
		    //System.out.println("xunta43 3: docFreq:"+docFreq+"|numDocs: "+numDocs);
//return 1;
	    return (float)(Math.log(numDocs/(double)(docFreq+1)) + 1.0);
	  }

}


	
	






