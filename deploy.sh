#!/usr/bin/env bash

gcloud functions deploy test-function \
  --project functions-terraform \
  --region europe-west3 \
  --entry-point tobias.function.Function \
  --runtime java11 \
  --trigger-http \
  --memory 512MB
