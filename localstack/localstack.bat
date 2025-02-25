@echo off

echo ### Criando Chaves no AWS Parameter Store do LocalStack...
aws --endpoint http://localhost:4566 --profile localstack ssm put-parameter --name "localstack" --value "Hello World Parameter Store" --type String

echo ### Criando Segredos no AWS Secret Manager do LocalStack...
aws --endpoint-url http://localhost:4566 --profile localstack secretsmanager create-secret --name "MySecrets" --description "Exemplo de Secrets Manager" --secret-string "{\"accessKey\":\"accessKey\", \"secretKey\":\"secretKey\"}"

echo ### Criando Bucket no S3 do LocalStack...
aws --endpoint http://localhost:4566 --profile localstack s3 mb s3://my-bucket

echo ### Criando Queue(Standard) no SQS do LocalStack...
aws --endpoint http://localhost:4566 --profile localstack sqs create-queue --queue-name MyQueue
aws --endpoint http://localhost:4566 --profile localstack sqs send-message --queue-url http://localhost:4566/000000000000/MyQueue --message-body "Hello World SQS!!!" --delay-seconds 1
aws --endpoint http://localhost:4566 --profile localstack sqs receive-message --queue-url http://localhost:4566/000000000000/MyQueue

echo ### Criando Queue(Standard) no SNS do LocalStack...
aws --endpoint http://localhost:4566 --profile localstack sns create-topic --name MyTopic
aws --endpoint http://localhost:4566 --profile localstack sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:MyQueue --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:MyQueue
