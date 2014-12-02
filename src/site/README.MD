# Automated Directory Watcher.

I created this application to watch a directory structure for change and then execute some kind of command. I like Guard but the problem with Guard is it's requirement for Ruby and my environment doesnt allow Ruby, just java.

NOTE: this application requires at a minimum Java 7

## Parameters

    -action "C:\\cygwin\\bin\\bash.exe --login /cygdrive/c/hello.sh"
    -recursive
    -workingdir "C:\cygwin\bin"
    -sensitivity 10000
    -dir "c:\temp\deep\"