package com.example;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CommandUtilityVO {
	
	
	public String genarateData(List<StreamDataVO> streamDataVOList){		
	
    	String data = null;    	
    	String output = null;
    	
		try{			
			
		StreamDataVO streamDataVO = streamDataVOList.get(0);
		
		String sourceResp= getSource(streamDataVO);		
		String processorResp=getProcessor(streamDataVO);
		String sinkResp= getSink(streamDataVO);
		
		StringBuffer addspaceBuffer= new StringBuffer();		
		addspaceBuffer.append(ApplicationConstants.SPACE);
		addspaceBuffer.append(ApplicationConstants.TAP);
		addspaceBuffer.append(ApplicationConstants.SPACE);
		
		String addstring=addspaceBuffer.toString();
		
		data = sourceResp+ addstring+processorResp+addstring+sinkResp;		
		
		output=consolidatedData(data, streamDataVO);
		
		
		System.out.println("output------------------>"+output);
		}
    	catch(Exception e){
    		e.printStackTrace();
    	}
		return output;
	}


	private static String getSource(StreamDataVO streamInput){
		
       StreamAttributesVO sourceAttributesVO =  streamInput.getSource();		
		StringBuffer sourcedata=new StringBuffer();			
		sourcedata.append(sourceAttributesVO.getType());		
		List<StreamAttributesValueVO> sourceAttributesValueVO = sourceAttributesVO.getAttributes();		
		String output=create(sourceAttributesValueVO);		
		sourcedata.append(output);
		
		return sourcedata.toString();
	}
	
	private static String getProcessor(StreamDataVO streamInput){
		
	       StreamAttributesVO processorAttributesVO =  streamInput.getProcessor();
			StringBuffer processorData=new StringBuffer();			
			processorData.append(processorAttributesVO.getType());			
			List<StreamAttributesValueVO> processorAttributesValueVO = processorAttributesVO.getAttributes();			
			String output=create(processorAttributesValueVO);
			processorData.append(output);
			
			return processorData.toString();
		}
		
	private static String getSink(StreamDataVO streamInput){
		
	       StreamAttributesVO sinkAttributesVO =  streamInput.getSink();	
		   StringBuffer sinkData=new StringBuffer();			
			sinkData.append(sinkAttributesVO.getType());			
			List<StreamAttributesValueVO> sinkAttributesValueVO = sinkAttributesVO.getAttributes();			
			String output=create(sinkAttributesValueVO);
			sinkData.append(output);
			
			return sinkData.toString();
		}
	
	private static String create(List<StreamAttributesValueVO> attributesValueVOList){
		
		    StringBuffer commonformat=new StringBuffer();
		    
			for(StreamAttributesValueVO streamattributes : attributesValueVOList){	
				
				commonformat.append(ApplicationConstants.SPACE);
				commonformat.append(ApplicationConstants.HYPENS);				
				if(streamattributes.getKey().equalsIgnoreCase("port")){
					commonformat.append(ApplicationConstants.SERVER);
					commonformat.append(ApplicationConstants.DOT);
					commonformat.append(streamattributes.getKey());					
				}
				else{
					commonformat.append(streamattributes.getKey());
				}		
				commonformat.append(ApplicationConstants.EQUALSTO);				
				if(streamattributes.getKey().equalsIgnoreCase("inputs") || streamattributes.getKey().equalsIgnoreCase("outputs")
						||streamattributes.getKey().equalsIgnoreCase("inputType") || streamattributes.getKey().equalsIgnoreCase("outputType") ){
					commonformat.append(ApplicationConstants.SINGLEQUOTE);
					commonformat.append(streamattributes.getValue());
					commonformat.append(ApplicationConstants.SINGLEQUOTE);
				}
			   else{
					commonformat.append(streamattributes.getValue());
				}
			
			}
			
			return commonformat.toString();
		}
	
    private String consolidatedData(String data,  StreamDataVO streamDataVO) {
		
		StringBuffer cumulativeBuffer = new StringBuffer();
		cumulativeBuffer.append(ApplicationConstants.NAME);
		cumulativeBuffer.append(ApplicationConstants.EQUALSTO);
		cumulativeBuffer.append(streamDataVO.getStreamname());
		cumulativeBuffer.append(ApplicationConstants.AMBERSENT);
		cumulativeBuffer.append(ApplicationConstants.DEFINITION);
		cumulativeBuffer.append(ApplicationConstants.EQUALSTO);
		cumulativeBuffer.append(data);
		cumulativeBuffer.append(ApplicationConstants.AMBERSENT);
		cumulativeBuffer.append(ApplicationConstants.DEPLOY);
		cumulativeBuffer.append(ApplicationConstants.EQUALSTO);
		
		cumulativeBuffer.append(streamDataVO.getDeploy());
		
		return cumulativeBuffer.toString();
	}
}
