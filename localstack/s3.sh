#!/bin/sh
echo "Init localstack s3"
awslocal s3 mb s3://i_can_has_bucket
awslocal s3 mb s3://blifood-local