"""Module with code to be run before and after certain events during the testing."""
import json
import datetime
import subprocess
import os.path

from behave.log_capture import capture
import requests
import time
import sys
import signal

# The following API endpoint is used to check if the system is started
_API_ENDPOINT = 'api/'


def _teardown_system(context):
    if context.lein_pid is not None:
        print("Terminating process {proc}".format(proc=context.lein_pid))
        os.killpg(context.lein_pid, signal.SIGTERM)


def _wait_for_system(context, wait_for_server=60):
    start = datetime.datetime.now()
    wait_till = start + datetime.timedelta(seconds=wait_for_server)
    # try to wait for server to start for some time
    while datetime.datetime.now() < wait_till:
        print("waiting for the server")
        time.sleep(1)
        started_all = False
        if _is_running(context):
            started_all = True
            break
    if started_all:
        # let's give the whole system a while to breathe
        print("breathing")
        time.sleep(5)
        print("done")
    else:
        raise Exception('Server failed to start in under {s} seconds'.
                        format(s=wait_for_server))


def _start_system(context):
    try:
        command = ["lein", "run"]
        child = subprocess.Popen(command, preexec_fn=os.setsid)
        context.lein_pid = child.pid
        print("Server is running as PID {proc}".format(proc=context.lein_pid))
    except subprocess.CalledProcessError as e:
        raise Exception('Failed to start or restart the system.')


def _restart_system(context, wait_for_server=60):
    _teardown_system(context)
    _start_system(context)
    _wait_for_system(context, wait_for_server)


def _is_running(context, accepted_codes=None):
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
    context.lein_pid = None
    context.teardown_system = _teardown_system
    context.restart_system = _restart_system
    context.is_running = _is_running

    context.api_url = "http://localhost:3000/" + _API_ENDPOINT
    _restart_system(context)


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
