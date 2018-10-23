package org.jenkinsci.plugins.database.Sample;

def f = namespace(lib.FormTagLib)

f.section(title:"Framework Database Configuration ===") {
    f.dropdownDescriptorSelector(field:"database",title:_("Database"))
}
