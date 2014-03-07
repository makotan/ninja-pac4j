ninja-pac4j
===========

ninja pac4j module

ninja > 3.x.x

pac4j == 1.4.1


setup
===========

git clone して maven install

pom.xml
---------------

これを追加

    <dependency>
        <groupId>ninja.auth.pac4j</groupId>
        <artifactId>ninja-pac4j</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>

バージョンは最新を

その他に pac4j-xxx が必要なので追加する

1.4.1はOK、1.5はNG(APIの変更に未対応)

application.conf
---------------

access error 時にlogin画面に飛ばすためのredirect先

    pac4j.auth_error_redirect=/login

defaultのClientName(option)

    pac4j.client_name=FormClient

loginがOKの時のredirect先

    pac4j.default_redirect=/


ClientsFactory
---------------

ClientsFactoryをimplementsしたclassを追加する

必要な各種Clientはここに定義

TwitterやFacebookなどのClientに必要な情報は、このクラスでapplication.confや環境変数などから取得する


ProfileAccess
---------------

ProfileAccessをimplementsしたclassを追加する

CommonProfileのTemporary Storageとして利用する為

実際は Ninja’s Cache Api を利用してCacheにする、DynamoDBを利用するなどの方法で実装する

Module
---------------

ClientFactoryをimplementsしたclassとProfileAccessをimplementsしたclassの定義を追加する

    bind(ClientsFactory.class).to(MyClientsFactory.class);
    bind(ProfileAccess.class).to(SampleProfileAccess.class);


Routes
---------------
CallbackContorllerのcallbackへPOSTを設定する

    router.POST().route("/callback").with(CallbackController.class , "callback");


Controller
---------------
Pac4jFilter.classをFilterとして追加する

    @FilterWith({Pac4jFileter.class})


