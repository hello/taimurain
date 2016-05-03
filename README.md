# taimurain
タイムライン – Timeline service

##Installation:##
1. sudo apt-get update 
2. clone or copy repo onto target instance 
3. sudo apt-get install  python-dev python-pip python-nose g++ libopenblas-dev liblapack-dev git libhdf5-serial-dev gfortran
4. sudo pip install -r ./requirements.txt

## Running the App ##
1. Make sure you have amazon credentials that have access to s3://hello-neuralnet-models
2. sudo ./app.py -p PORT -c ./config.prod.ini


