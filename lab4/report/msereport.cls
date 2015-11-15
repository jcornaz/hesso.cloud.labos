\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{msereport}[2015/10/10 Rapport pour laboratoires et projets du MSE de la HES-SO]
\LoadClass[a4paper]{article}

\RequirePackage[utf8]{inputenc}
\RequirePackage[T1]{fontenc}
\RequirePackageWithOptions{isodate}
\RequirePackageWithOptions{babel}

\RequirePackage{graphicx}
\RequirePackage{fancyhdr}
\RequirePackage[a4paper, top=25mm]{geometry}
\RequirePackage{lastpage}
\RequirePackage[hidelinks]{hyperref}
\RequirePackage{acronym}
\RequirePackage{titlesec}
\RequirePackage{parskip}
\RequirePackage[usenames,dvipsnames]{xcolor}

\definecolor{DarkGray}{gray}{0.3}


\setlength{\parskip}{10pt}

\DeclareUnicodeCharacter{00A0}{~}

\let\stdsection\section
\renewcommand\section{\clearpage\stdsection}

% A section with subtitle
\newcommand{\subtitledsection}[2][]{
	\section[#2]{#2 \\ \small #1}
}

% Start the appendix
\newcommand{\appendixsection}[1][Annexes]{
	\section{#1}
	\appendix
	\renewcommand{\thesubsection}{\Alph{subsection}}
}

% The module concerned by the report
\newcommand{\themodule}{}
\newcommand{\themodulefullname}{}
\newcommand{\module}[2]{
	\renewcommand{\themodule}{#1}
	\renewcommand{\themodulefullname}{#2}
}

% Display of a brand
\newcommand{\brand}[1]{\textrm{\emph{#1}}}

\newcommand{\code}[1]{\texttt{#1}}

% Definition of a new acronym
\newcommand{\defacronym}[2]{\acro{#1}[#1]{#2}}

% Anglisism
\newcommand{\eng}[1]{\textit{#1}}

% Set the default font family to helvetica
\renewcommand{\familydefault}{phv}

% Page header and footer
\pagestyle{fancy}
\setlength{\headheight}{20pt}

\fancyhead{}
\fancyfoot{}

\fancyhead[L]{\footnotesize{\@title}}
\fancyhead[R]{\includegraphics[width=50mm]{mse.jpg}}

\fancyfoot[L]{\footnotesize{\textit{\@author}}}
\fancyfoot[R]{\footnotesize{\thepage\ / \pageref{LastPage}}}


\renewcommand{\maketitle}{
	\pagenumbering{alph}
	
	\centering
	
	\vspace*{\fill}
	
	\huge{\@title}
	
	
	\vspace{5mm}
	
	\Large{\@author}
	
	\vspace{10mm}
	
	\LARGE{\textit{\themodulefullname}}
	
	\vspace{15mm}
	\includegraphics[width=80mm]{mse.jpg}
	
	\vspace{15mm}
	\includegraphics[width=80mm]{hes-so.jpg}
	
	\vspace*{\fill}
	
	\large{\textit{\@date}}
	
	\clearpage
	
	\pagenumbering{arabic}
}

\AtBeginDocument{
	
	\begin{titlepage}
		\maketitle		
	\end{titlepage}
		
	\clearpage
	\tableofcontents
	\clearpage
}

\renewcommand\listoffigures{%
	\subsection{\listfigurename}%
	\@mkboth{\MakeUppercase\listfigurename}%
	{\MakeUppercase\listfigurename}%
	\@starttoc{lof}%
}

\renewcommand\listoftables{%
	\subsection{\listtablename}%
	\@mkboth{\MakeUppercase\listtablename}%
	{\MakeUppercase\listtablename}%
	\@starttoc{lot}%
}

\let\EndItemize\enditemize
\def\enditemize{\EndItemize\bigskip}