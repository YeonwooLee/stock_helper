import win32com.client
# 연결 여부 체크 - 0 = 연결ㄴㄴ, 1 = 연결 ㅇㅇ / 현재 정상 작동 안함
def connectionOk():
    objCpCybos = win32com.client.Dispatch("CpUtil.CpCybos")
    bConnect = objCpCybos.IsConnect
    if (bConnect == 0):
        # print("PLUS가 정상적으로 연결되지 않음. ")
        raise ConnectionError("NO_CYBOS_CONNECTION_ERROR - Cybos Plus가 정상적으로 연결되지 않음")
        
    return True