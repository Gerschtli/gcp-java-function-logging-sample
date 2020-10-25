#!/usr/bin/env bash
set -euo pipefail

./gradlew :function:shadowJar

gcloud functions deploy function-logging-sample \
  --project functions-terraform \
  --region europe-west3 \
  --entry-point tobias.function.Function \
  --runtime java11 \
  --trigger-http \
  --memory 512MB \
  --source function/build/libs
