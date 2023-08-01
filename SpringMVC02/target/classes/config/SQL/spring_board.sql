drop table spring_board;

create table spring_board(
   num number(8) primary key, --글번호
   name varchar2(30) not null, --작성자
   passwd varchar2(20) not null, --글비밀번호
   subject varchar2(200) not null,--글제목
   content varchar2(2000), --글내용
   wdate date default sysdate, --작성일
   readnum number(8) default 0, --조회수
   filename varchar2(500),--첨부파일명[uuid_a.txt] ==> 물리적 파일명(실제 업로드된 파일명)
   originFilename varchar2(500),--원본파일명 [a.txt]==> 논리적 파일명
   filesize number(8),--첨부파일 크기
   refer number(8), --글 그룹번호[답변형 게시판일때 필요한 컬럼]
   lev number(8), --답변 레벨[답변형 게시판일때 필요한 컬럼]
   sunbun number(8) -- 같은 글 그룹 내에 순서 정렬시 필요한 컬럼 [답변형 게시판일때 필요한 컬럼]
);

drop sequence spring_board_seq;

create sequence spring_board_seq
start with 1
increment by 1
nocache;


