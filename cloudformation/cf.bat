aws --endpoint http://localhost:4566 --profile localstack cloudformation deploy --stack-name infra-s3-sqs-sns --template-file "infra-s3-sqs-sns.yml"
aws --endpoint http://localhost:4566 --profile localstack cloudformation deploy --stack-name infra-parameter-store --template-file "infra-parameter-store.yml"
aws --endpoint http://localhost:4566 --profile localstack cloudformation deploy --stack-name infra-rds --template-file "infra-rds.yml"
aws --endpoint http://localhost:4566 --profile localstack cloudformation deploy --stack-name infra-ec2 --template-file "infra-ec2.yml"
aws --endpoint http://localhost:4566 --profile localstack cloudformation delete-stack --stack-name infra-s3-sqs-sns
aws --endpoint http://localhost:4566 --profile localstack cloudformation delete-stack --stack-name infra-parameter-store
aws --endpoint http://localhost:4566 --profile localstack cloudformation delete-stack --stack-name infra-rds
aws --endpoint http://localhost:4566 --profile localstack cloudformation delete-stack --stack-name infra-ec2
