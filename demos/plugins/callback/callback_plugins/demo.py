from ansible.plugins.callback import CallbackBase
from pyfiglet import Figlet


class CallbackModule(CallbackBase):

    def __init__(self):
        super(CallbackModule, self).__init__()

    def runner_on_ok(self, host, res):
        if not res['changed']:
            f = Figlet(font='starwars')
            print f.renderText('Nothing changed')
