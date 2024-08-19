package me.kumo.nbt;

public enum NbtTerminalExamples {
    TRAVERSE("""
            // you can print stuff with print() and access selected node with 'data'
            function traverseObject(obj, level) {
                var indentation = ' '.repeat(level * 2);
                for(var key in obj) {
                    if (obj.hasOwnProperty(key)) {
                        if (typeof obj[key] === 'object' && obj[key] !== null) {
                            print(indentation+key+': ');
                            traverseObject(obj[key], level + 1);
                        } else {
                            print(indentation+key+': '+obj[key]);
                        }
                    }
                }
            }
            traverseObject(data, 0);
            """);

    public final String code;

    NbtTerminalExamples(String code) {
        this.code = code;
    }
}
