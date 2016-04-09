from boto.s3.connection import S3Connection
import os
from keras.models import model_from_json
import logging
import string
import random

def id_generator(size=6, chars=string.ascii_uppercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))

def get_models_from_s3(bucket):
    configs = {}
    values = {}

    conn = S3Connection(os.getenv('AWS_ACCESS_KEY_ID'),os.getenv('AWS_SECRET_KEY'))
    bucket = conn.get_bucket(bucket)
    for key in bucket.list():
        name = key.name.encode('utf-8')
        if 'json' in name:
            logging.info('downloading %s/%s' % (bucket,name))
            value = bucket.get_key(name)
            model_config = value.get_contents_as_string()
            try:
                values_name = name.replace('json','h5')
                logging.info('downloading %s/%s' % (bucket,values_name))
                model_values = bucket.get_key(values_name)
                values[name] = values_name,model_values
                configs[name] = model_config
            except Exception:
                logging.error('fail on %s' % name)

    models = {}
    for key in configs:
        logging.info('compiling model %s ...' % key)
        models[key] = model_from_json(configs[key])            
    logging.info('DONE COMPILING')

    logging.info('LOADING WEIGHTS')
    for key in configs:
        vname,data = values[key]
        filename = vname + '.' + id_generator(16)
        with open(filename,'w') as f:
            #hacky as fuck
            f.write(data.get_contents_as_string())

        models[key].load_weights(filename)
        os.remove(filename)

    return models
        
if __name__ == '__main__':
    get_models_from_s3('hello-neuralnet-models')
