#!/bin/sh
echo "[ Starting Build_Deploy Script ]"
echo "================================================================="
echo "Set variables"
export APPLICATION_NAME=$top_directory
export DOCKERTAG=dev.${DEV_AUTHOR}.1.${BUILD_ID}
export DOCKERIMAGE=SVSACCODOCKER.CO-OPBANK.CO.KE:5000/microservices/$APPLICATION_NAME:${DOCKERTAG}

echo "================================================================="

echo APPLICATION_NAME $APPLICATION_NAME
echo DOCKERTAG $DOCKERTAG
echo DOCKERIMAGE $DOCKERIMAGE
echo "Print Working Directory" $Working_Directory

echo "================================================================="

chmod -R +x $Working_Directory
cd $Working_Directory
ls -lrt
echo ""
echo "-----------------------------------------------------------------"

sudo ./target/self-onboarding.jar app.jar

sudo docker build -t $DOCKERIMAGE .

echo "-----------------------------------------------------------------" 

echo "[ Check if the docker build tag exists ] ... $DOCKERIMAGE"
if [[ -z $(sudo docker images | grep ${APPLICATION_NAME} | grep ${DOCKERTAG}) ]] ; then echo "ERROR! Image was not created ---" && exit 1 ; else echo " BINGO! Image successfully created ---" ; fi

sudo docker images | grep ${APPLICATION_NAME} | grep v2.0.${BUILD_ID}.dev

echo "-----------------------------------------------------------------" 
sudo docker push $DOCKERIMAGE
cd $Working_Directory/kubernetes/

ls deployment.yaml | while read file ; do mv $file $(echo $file | sed -e 's#deployment#'"$APPLICATION_NAME"'-deployment#g') ; done
sed -i s#APPLICATION_NAME#"$APPLICATION_NAME"#g $APPLICATION_NAME-deployment.yaml
sed -i s#DOCKERIMAGE#"$DOCKERIMAGE"#g $APPLICATION_NAME-deployment.yaml

echo "-----------------------------------------------------------------" 
ls -lrt
echo ""
echo "-----------------------------------------------------------------" 

sudo kubectl delete -f $APPLICATION_NAME-deployment.yaml  || echo "skip if image does not exist"
sudo kubectl apply -f $APPLICATION_NAME-deployment.yaml

echo "-----------------------------------------------------------------" 

echo "[ END OF DEPLOYMENT ] ... check if pod is running "
sleep 15
#pod_status=$(sudo kubectl get pods | grep $APPLICATION_NAME | awk -F " " '{print$03}' | sort | uniq -d)
pod_status=$(sudo kubectl get pods | grep $APPLICATION_NAME | awk -F " " '{print$03}')
if [[ $pod_status == "Running" ]]; then echo "IMAGE RUNNING SUCCESSFULLY" ; else echo "ERROR :: IMAGE NOT RUNNING" ;fi

echo "-----------------------------------------------------------------" 
echo ""
sudo kubectl describe deployment/$APPLICATION_NAME || echo "skip if error"