		var version="";   //版本
  		var semester="";	//学期
  		var cid="";			//课程编号	
  		//生成班级列表
  		function createClassOption(){
  			semester=$('input[name="semester"]:checked').val();
  			$.ajax({
  				url:"/Examination2.0/initData_examClass.action",
  				type:"post",
  				datatype:"json",
  				success:function(data){
  				    
  					var examineeClassList=$.parseJSON(data);
  					var optionstr="";	
  					if(examineeClassList.responseCode!=1){
  						for(var i=0;i<examineeClassList.obj.length;i++){
  							optionstr+="<option value='"+examineeClassList.obj[i].id+"' name='className'>"+examineeClassList.obj[i].className+"</option>";
  							if(examineeClassList.obj[i].currentUse==1){
  								currentUse=examineeClassList.obj[i].id;
  							}
  						}
  					}else{
  						optionstr+="<option value=0 >没有数据</option>"
  					}	
  					$('#classes').html(optionstr);
  				}
  			});
  		}
  		
  		/*function createClassOption(){
            semester=$('input[name="semester"]:checked').val();
            $.ajax({
                url:"/Examination2.0/initData_examClass.action",
                type:"post",
                data:{"semester":semester},
                datatype:"json",
                success:function(data){
                    
                    var examineeClassList=$.parseJSON(data);
                    var optionstr="";   
                    if(examineeClassList.responseCode!=1){
                        for(var i=0;i<examineeClassList.obj.length;i++){
                            optionstr+="<option value='"+examineeClassList.obj[i].id+"' name='className'>"+examineeClassList.obj[i].className+"</option>";
                            if(examineeClassList.obj[i].currentUse==1){
                                currentUse=examineeClassList.obj[i].id;
                            }
                        }
                    }else{
                        optionstr+="<option value=0 >没有数据</option>"
                    }   
                    $('#classes').html(optionstr);
                }
            });
        }*/
  		
  		//生成版本列表
  		function createVersionOption(){
  		var currentUse="";
  		 $.ajaxSettings.async = false; 
  			$.ajax({
  				url:"/Examination2.0/initData_version.action",
  				type:"post",
  				datatype:"json",
  				success:function(data){
  				    
  					var examineeClassList=$.parseJSON(data);
  					var optionstr="";	
  					if(examineeClassList.responseCode!=1){
  						for(var i=0;i<examineeClassList.obj.length;i++){
  							optionstr+="<option value='"+examineeClassList.obj[i].id+"' name='className'>"+examineeClassList.obj[i].editionName+"</option>";
  							if(examineeClassList.obj[i].currentUse==1){
  								currentUse=examineeClassList.obj[i].id;
  							}
  						}
  					}else{
  						optionstr+="<option value=0 >没有数据</option>"
  					}	
  					$("#version").html(optionstr);
  					$("#version option[value="+currentUse+"]").attr("selected",true)
  					
  				}
  				
  			});
  			
  		}
  		//生成科目列表
  		function createSelectOption(version,semester){
  		 $.ajaxSettings.async = false; 
  			$.ajax({
  				url:"/Examination2.0/initData_subject.action",
  				type:"post",
  				data:{"editionId":version,"semester":semester},
  				datatype:"json",
  				success:function(data){
  					var examineeClassList=$.parseJSON(data);
  					var optionstr="";
  					if(examineeClassList.responseCode!=1){
  						for(var i=0;i<examineeClassList.obj.length;i++){
  						optionstr+="<option value='"+examineeClassList.obj[i].id+"'>"+examineeClassList.obj[i].subjectName+"</option>";
  						}
  					}else{
  						optionstr+="<option value=0 >没有数据</option>"
  					}
  					
  					$("#subject").html(optionstr);
  				}
  			});
  		}
  		//生成章节列表
  		function createChapterOption(subjectId){
  	  		 $.ajaxSettings.async = false; 
  	  			$.ajax({
  	  				url:"/Examination2.0/initData_chapter.action",
  	  				type:"post",
  	  				data:{"subjectId":subjectId},
  	  				datatype:"json",
  	  				success:function(data){
  	  					var examineeClassList=$.parseJSON(data);
  	  					var optionstr="";
  	  					if(examineeClassList.responseCode!=1){
  	  						for(var i=0;i<examineeClassList.obj.length;i++){
  	  						optionstr+="<option value='"+examineeClassList.obj[i].id+"'>"+examineeClassList.obj[i].chapterName+"</option>";
  	  						}
  	  					}else{
  	  						optionstr+="<option value=0 >没有数据</option>"
  	  					}
  	  					
  	  					$("#chapter").html(optionstr);
  	  				}
  	  			});
  	  		}
  		
  		//生成所有科目列表
  		function findAllSubject(){
  			$.ajaxSettings.async = false; 
  			$.ajax({
  				url:"/Examination2.0/initData_subject.action",
  				type:"post",
  				data:{"editionId":version,"semester":semester},
  				datatype:"json",
  				success:function(data){
  					var examineeClassList=$.parseJSON(data);
  					var optionstr="";
  					if(examineeClassList.responseCode!=1){
  						for(var i=0;i<examineeClassList.obj.length;i++){
  						optionstr+="<option value='"+examineeClassList.obj[i].id+"'>"+examineeClassList.obj[i].subjectName+"</option>";
  						}
  					}else{
  						optionstr+="<option value=0 >没有数据</option>"
  					}
  					
  					$("#subject").html(optionstr);
  				}
  			});
  			
  		}
  		//查出所有班级信息
  		function findexamineeClass(){
  	  		semester=$('input[name="semester"]:checked').val();
  	  		$.ajaxSettings.async = false; 
  	  			$.ajax({
  	  				url:"/Examination2.0/initData_examClass.action",
  	  				type:"post",
  	  				datatype:"json",
  	  				success:function(data){
  	  				    
  	  					var examineeClassList=$.parseJSON(data);
  	  					var optionstr="";	
  	  					if(examineeClassList.responseCode!=1){
  	  						for(var i=0;i<examineeClassList.obj.length;i++){
  	  							optionstr+="<option value='"+examineeClassList.obj[i].id+"' name='className'>"+examineeClassList.obj[i].className+"</option>";
  	  							if(examineeClassList.obj[i].currentUse==1){
  	  								currentUse=examineeClassList.obj[i].id;
  	  							}
  	  						}
  	  					}else{
  	  						optionstr+="<option value=0 >没有数据</option>"
  	  					}	
  	  					$('#classes').html(optionstr);
  	  				}
  	  			});
  	  	}
  		