package org.jenkinsci.plugins.database.RootDatabaseConsole

import java.sql.ResultSet;
import org.jvnet.hudson.test.mybatis.User;

def f = namespace(lib.FormTagLib)
def l = namespace(lib.LayoutTagLib)

l.layout{
    l.main_panel {
        form(method:"post",action:"execute") {
            raw("""
<p>
    Go to <a href="../configure">the system config page</a> and configure
    a valid database connection. Also try installing some database driver plugins,
    such as <tt>database-h2</tt>.
</p><p>
    Then come back to this console and execute arbitrary SQL against the configured database.
</p>
<h2>SQL</h2>
<textarea name=sql style='width:100%; height:5em'></textarea>
            """)
            div {
                f.submit(value:"Execute")
            }
        }

        if (request.getAttribute("message")!=null) {
            p(message)
        }

        if (request.getAttribute("r")!=null) {
            // renders the result
            h2("Result")
            table {
                java.util.List rs = r;
                int count = rs.size();//.metaData.columnCount;

                tr {
                   // for (int i=1; i<=5; i++) {
                        th { "아이디" }
						th { "이메일" }
						th { "패스" }
						th { "이름1" }
						th { "이름2" }
                   // }
                }

                for(int i=0;i<rs.size();i++) {
					User user = rs.get(i);
                    tr {
                        //for (int i=1; i<=count; i++) {
                            td(user.getUserId());
							td(user.getEmailId());
							td(user.getPassword());
							td(user.getFirstName());
							td(user.getLastName());
                        //}
                    }
                }
            }
        }
    }
}

