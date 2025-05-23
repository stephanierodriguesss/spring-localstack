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

echo ### Criando tabela Dynamo no LocalStack...
aws --endpoint-url=http://localhost:4566 dynamodb create-table --table-name Users --attribute-definitions AttributeName=id,AttributeType=S --key-schema AttributeName=id,KeyType=HASH --billing-mode PAY_PER_REQUEST

echo ### Criando metricas...
aws --endpoint-url=http://localhost:4566 cloudwatch put-metric-data --namespace "MyAppMetrics" --metric-name "TestMetric"  --value 1  --dimensions Name=Service,Value=TestService

echo ### Criando police...
aws --endpoint-url=http://localhost:4566 iam create-policy --policy-name LocalStackFreePolicy --policy-document file://localstack-free-policy.json

echo ### Criando user IAM...
aws --endpoint-url=http://localhost:4566 iam create-user --user-name localstack-user

echo ### Anexando policy ao user IAM...
aws --endpoint-url=http://localhost:4566 iam create-user --user-name localstack-user

echo ### Criando credentials para a app...
aws --endpoint-url=http://localhost:4566 iam create-access-key --user-name localstack-user
