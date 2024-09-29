FROM mcr.microsoft.com/windows/servercore:ltsc2022
LABEL authors="atara"

ENTRYPOINT ["powershell", "Start-Process"]
