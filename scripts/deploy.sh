#!/usr/bin/env bash

REPOSITORY="/home/ubuntu/heyhi"
TIME_NOW=$(date +%c)

APP_NAME=heyhello
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep SNAPSHOT.jar | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

DEPLOY_LOG="$REPOSITORY/deploy.log"

function get_pid() {
  ps aux | grep java | grep $APP_NAME | grep -v grep | awk '{print $2}'
}

CURRENT_PID=$(get_pid)

echo "왜 PID 출력 안됨????? 출력해봐! [$CURRENT_PID]"
echo "[$TIME_NOW] > 현재 실행 중인 .jar PID: $CURRENT_PID" >> $DEPLOY_LOG

if [ -z $CURRENT_PID ]
then
  echo "[$TIME_NOW] > 실행 중인 애플리케이션이 없습니다." >> $DEPLOY_LOG
else
  echo "[$TIME_NOW] > PID: $CURRENT_PID 종료." >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 10
  if ps -f $CURRENT_PID > /dev/null
  then
    echo "[$TIME_NOW] > 프로세스가 정상정료되지 않으므로 강제종료 합니다." >> $DEPLOY_LOG
    kill -9 $CURRENT_PID
fi

echo "[$TIME_NOW] > $JAR_PATH 배포" >> $DEPLOY_LOG
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &

CURRENT_PID=$(get_pid)
echo "[$TIME_NOW] > 재배포된 .jar PID: $CURRENT_PID" >> $DEPLOY_LOG
