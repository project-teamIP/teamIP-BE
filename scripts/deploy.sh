#!/usr/bin/env bash

function get_pid() {
  pgrep -f $APP_NAME
}

REPOSITORY="/home/ubuntu/heyhi"
TIME_NOW=$(date +%c)

APP_NAME=heyhello
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME
CURRENT_PID=$(get_pid)

DEPLOY_LOG="$REPOSITORY/deploy.log"

echo "[$TIME_NOW] > 현재 실행 중인 .jar PID: $CURRENT_PID" >> $DEPLOY_LOG

if [ -z $CURRENT_PID ]
then
  echo "[$TIME_NOW] > 실행 중인 애플리케이션이 없습니다. <" >> $DEPLOY_LOG
else
  echo "[$TIME_NOW] > 실행 중인 $CURRENT_PID 종료합니다. <" >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "[$TIME_NOW] > $JAR_PATH 배포" >> $DEPLOY_LOG
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &

CURRENT_PID=$(get_pid)
echo "[$TIME_NOW] > 재배포된 .jar PID: $CURRENT_PID" >> $DEPLOY_LOG
