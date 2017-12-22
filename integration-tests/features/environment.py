"""Module with code to be run before and after certain events during the testing."""
import json
import datetime
import subprocess
import os.path

from behave.log_capture import capture
import requests
import time
import sys

# The following API endpoint is used to check if the system is started
_API_ENDPOINT = 'api/'


def _teardown_system(context):
    print("teardown")


def _wait_for_system(context, wait_for_server=60):
    print("wait for system")


def _restart_system(context, wait_for_server=60):
    print("restart system")


def _is_running(context, accepted_codes = None):
    accepted_codes = accepted_codes or {200, 401}
    url = context.api_url
    try:
        res = requests.get(url)
        if res.status_code in accepted_codes:
            return True
    except requests.exceptions.ConnectionError:
        pass
    return False


def before_all(context):
    """Perform the setup before the first event."""
    context.teardown_system = _teardown_system
    context.restart_system = _restart_system
    context.is_running = _is_running

    context.api_url = "http://localhost:3000/" + _API_ENDPOINT


@capture
def before_scenario(context, scenario):
    """Perform the setup before each scenario is run."""
    pass


@capture
def after_scenario(context, scenario):
    """Perform the cleanup after each scenario is run."""
    pass


@capture
def after_all(context):
    """Perform the cleanup after the last event."""
    try:
        _teardown_system(context)
    except subprocess.CalledProcessError as e:
        raise Exception('Failed to teardown system. Command "{c}" failed:\n{o}'.
                        format(c=' '.join(e.cmd), o=e.output))

