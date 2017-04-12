echo off
set arg1=%1
shift
shift
java -cp "LIR.jar;LIR_lib/*" org.thearlingtonplayers.reconcile %arg1%
