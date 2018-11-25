2016-11-11 edit by jaehyu

1. 임시로 intellij 환경을 올립니다.

- changes from eclipse 버젼
com.skt.ehs.mbs.db.maria.mapper 폴더 => resource 밑의 db.maria.mapper 로 이동

- mybatis-config.xml 파일에 해당내용 반영
<mappers>
 <mapper resource="db/maria/mapper/EHSUserMapper.xml"/>
 <mapper resource="db/maria/mapper/EHSMapInfoMapper.xml"/>
</mappers>


아래는 이 소스트리의 원본관련된 Readme 입니다

------ 아 래 ----------------------

해당 프로젝트는

Eclise + Maven을 기반으로 생성된 프로젝트이다.

개발 환경 설정
1. Git에서 해당 데이터를 가져온다.
2. Eclipse WorkSpace를 생성한다.
3. Import로 Maven 프로젝트를 로딩한다.
4. 해당 프로젝트를 Import한다.

주의사항
1. Elipse는 UTF-8을 기본 encoding으로 해주지 않고 있기 때문에 반드시 UTF-8로 설정을 해줘야한다.
참고주소
http://gangzzang.tistory.com/entry/%EC%9D%B4%ED%81%B4%EB%A6%BD%EC%8A%A4-%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD-UTF8-%EC%9D%B8%EC%BD%94%EB%94%A9-%EC%84%A4%EC%A0%95


