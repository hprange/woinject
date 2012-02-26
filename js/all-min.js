(function(){typeof(require)!="undefined"?SyntaxHighlighter=require("shCore").SyntaxHighlighter:null;
function Brush(){var keywords="if fi then elif else for do done until while break continue case function return in eq ne ge le";
var commands="alias apropos awk basename bash bc bg builtin bzip2 cal cat cd cfdisk chgrp chmod chown chrootcksum clear cmp comm command cp cron crontab csplit cut date dc dd ddrescue declare df diff diff3 dig dir dircolors dirname dirs du echo egrep eject enable env ethtool eval exec exit expand export expr false fdformat fdisk fg fgrep file find fmt fold format free fsck ftp gawk getopts grep groups gzip hash head history hostname id ifconfig import install join kill less let ln local locate logname logout look lpc lpr lprint lprintd lprintq lprm ls lsof make man mkdir mkfifo mkisofs mknod more mount mtools mv netstat nice nl nohup nslookup open op passwd paste pathchk ping popd pr printcap printenv printf ps pushd pwd quota quotacheck quotactl ram rcp read readonly renice remsync rm rmdir rsync screen scp sdiff sed select seq set sftp shift shopt shutdown sleep sort source split ssh strace su sudo sum symlink sync tail tar tee test time times touch top traceroute trap tr true tsort tty type ulimit umask umount unalias uname unexpand uniq units unset unshar useradd usermod users uuencode uudecode v vdir vi watch wc whereis which who whoami Wget xargs yes";
this.regexList=[{regex:/^#!.*$/gm,css:"preprocessor bold"},{regex:/\/[\w-\/]+/gm,css:"plain"},{regex:SyntaxHighlighter.regexLib.singleLinePerlComments,css:"comments"},{regex:SyntaxHighlighter.regexLib.doubleQuotedString,css:"string"},{regex:SyntaxHighlighter.regexLib.singleQuotedString,css:"string"},{regex:new RegExp(this.getKeywords(keywords),"gm"),css:"keyword"},{regex:new RegExp(this.getKeywords(commands),"gm"),css:"functions"}]
}Brush.prototype=new SyntaxHighlighter.Highlighter();
Brush.aliases=["bash","shell"];
SyntaxHighlighter.brushes.Bash=Brush;
typeof(exports)!="undefined"?exports.Brush=Brush:null
})();(function(){typeof(require)!="undefined"?SyntaxHighlighter=require("shCore").SyntaxHighlighter:null;
function Brush(){var keywords="abstract assert boolean break byte case catch char class const continue default do double else enum extends false final finally float for goto if implements import instanceof int interface long native new null package private protected public return short static strictfp super switch synchronized this throw throws true transient try void volatile while";
this.regexList=[{regex:SyntaxHighlighter.regexLib.singleLineCComments,css:"comments"},{regex:/\/\*([^\*][\s\S]*)?\*\//gm,css:"comments"},{regex:/\/\*(?!\*\/)\*[\s\S]*?\*\//gm,css:"preprocessor"},{regex:SyntaxHighlighter.regexLib.doubleQuotedString,css:"string"},{regex:SyntaxHighlighter.regexLib.singleQuotedString,css:"string"},{regex:/\b([\d]+(\.[\d]+)?|0x[a-f0-9]+)\b/gi,css:"value"},{regex:/(?!\@interface\b)\@[\$\w]+\b/g,css:"color1"},{regex:/\@interface\b/g,css:"color2"},{regex:new RegExp(this.getKeywords(keywords),"gm"),css:"keyword"}];
this.forHtmlScript({left:/(&lt;|<)%[@!=]?/g,right:/%(&gt;|>)/g})
}Brush.prototype=new SyntaxHighlighter.Highlighter();
Brush.aliases=["java"];
SyntaxHighlighter.brushes.Java=Brush;
typeof(exports)!="undefined"?exports.Brush=Brush:null
})();(function(){typeof(require)!="undefined"?SyntaxHighlighter=require("shCore").SyntaxHighlighter:null;
function Brush(){function process(match,regexInfo){var constructor=SyntaxHighlighter.Match,code=match[0],tag=new XRegExp("(&lt;|<)[\\s\\/\\?]*(?<name>[:\\w-\\.]+)","xg").exec(code),result=[];
if(match.attributes!=null){var attributes,regex=new XRegExp("(?<name> [\\w:\\-\\.]+)\\s*=\\s*(?<value> \".*?\"|'.*?'|\\w+)","xg");
while((attributes=regex.exec(code))!=null){result.push(new constructor(attributes.name,match.index+attributes.index,"color1"));
result.push(new constructor(attributes.value,match.index+attributes.index+attributes[0].indexOf(attributes.value),"string"))
}}if(tag!=null){result.push(new constructor(tag.name,match.index+tag[0].indexOf(tag.name),"keyword"))
}return result
}this.regexList=[{regex:new XRegExp("(\\&lt;|<)\\!\\[[\\w\\s]*?\\[(.|\\s)*?\\]\\](\\&gt;|>)","gm"),css:"color2"},{regex:SyntaxHighlighter.regexLib.xmlComments,css:"comments"},{regex:new XRegExp("(&lt;|<)[\\s\\/\\?]*(\\w+)(?<attributes>.*?)[\\s\\/\\?]*(&gt;|>)","sg"),func:process}]
}Brush.prototype=new SyntaxHighlighter.Highlighter();
Brush.aliases=["xml","xhtml","xslt","html"];
SyntaxHighlighter.brushes.Xml=Brush;
typeof(exports)!="undefined"?exports.Brush=Brush:null
})();