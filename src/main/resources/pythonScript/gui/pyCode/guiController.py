# -*- coding: utf-8 -*-
import pyautogui
import datetime
import sys
import time
import json
import os
# python -m pip install --upgrade pip
# pip install pyautogui
# pip install pip install pypiwin32
# pip install bs4
# pip install pynput
# pip install mouse
# pip install webdriver_manager
# pip install opencv-python
# pip install selenium
# pip install packaging


#화면에 img가 보이면 {true,imageposition}  리턴
def imgExistWithConfidence(img,confidenceRate):
    result = {'exist': False}
    imgPosition = pyautogui.locateOnScreen(img, confidence=confidenceRate)
    if imgPosition != None:
        result['exist'] = True
        result['imgPosition'] = imgPosition
    return result
# #화면에 img가 보이면 {true,imageposition}  리턴
# def imgExist(img):
#     result = {'exist': False}
#     imgPosition = pyautogui.locateOnScreen(img, confidence=0.95)
#     if imgPosition != None:
#         result['exist'] = True
#         result['imgPosition'] = imgPosition
#     return result


#이미지로 마우스 이동, 클릭 , 이미지 나타날때까지 대기
def mouseToImgAndClick(img,confidenceRate): #confidence = 일치율
    while True:
        imgExistRst = imgExistWithConfidence(img,confidenceRate)
        if imgExistRst['exist']:
            # print("imgPosition is = ", imgExistRst['imgPosition'])
            pyautogui.moveTo(imgExistRst['imgPosition'])
            pyautogui.click()
            break
        # print('searching')


#나타났다사라짐
def disappear(imgFileName,confidence):
    while not imgExistWithConfidence(imgFileName,confidence)['exist']:
        pass
        # print(imgFileName+" 의 등장을 기다리는중")
        # print(imgExistWithConfidence(imgFileName,confidence)['exist'])
    while imgExistWithConfidence(imgFileName,confidence)['exist']:
        pass

if __name__ == "__main__":
    script_path = os.path.abspath(__file__)

    # 현재 스크립트 파일이 있는 디렉토리 경로
    script_dir = os.path.dirname(script_path)
    # print("adfasdfasdf",script_dir)
    waitMax = int(sys.argv[1:][0])
    # print(waitMax)
#C:\\my2023programs\\TIL\\project\\stock_helper\\out\\production\\resources\\pythonScript\\gui\\pyCode
    
    confirm_exit_cp = script_dir+'\\image\\confirm_exit_cp.png'
    confirm_exit_cp_yes = script_dir+'\\image\\confirm_exit_cp_yes.png'
    # print(confirm_exit_cp)
    # print(confirm_exit_cp_yes)
    flag = False
    for sec in range(waitMax):
        conFirmExist = imgExistWithConfidence(confirm_exit_cp,0.85)['exist']
        if conFirmExist:
            # print(conFirmExist)
            mouseToImgAndClick(confirm_exit_cp_yes,0.85)
            flag = True
            break
        time.sleep(1)

    mouseToImgAndClick(script_dir+"\\image\\min_size_btn.png",0.85)

            
    if flag:
        result = '기존 프로그램 종료'
    else:
        result = '기존 프로그램 없음'

        

    print(json.dumps(result))