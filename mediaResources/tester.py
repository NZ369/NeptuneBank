from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from datetime import datetime, timedelta


def startProcess():
    driver = webdriver.Chrome()

    driver.get("http://localhost:4080/")

    driver.find_element_by_css_selector(
        "#header-tabs > li:nth-child(3) > a").click()

    driver.find_element_by_id("username").send_keys("newusertest")
    driver.find_element_by_id("email").send_keys("newusertestemail@gmail.com")
    driver.find_element_by_id("password").sen_keys("1234apples")
    driver.find_element_by_id("secondPassword").sen_keys("1234apples")

    driver.find_element_by_id("register-submit").click()


def main():
    startProcess()


'''
    result = -1
    while (result < 0):'''

'''
        if (result == 0):
            break'''


if __name__ == "__main__":
    main()
