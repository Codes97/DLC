var app = angular.module('appDlc', []);
// {{ }}
app.controller('mainController',
    function ($scope) {
        $scope.title = "Motor de Busqueda";
    }
); 


app.controller('BuscadorController',
    function ($scope, $http) {

        $scope.Titulo = 'Buscador';
        $scope.busquedaHecha = false;
    
        ///**FUNCIONES**///
        $scope.Search = function () {
            // las propiedades del params tienen que coincidir con el nombre de los parámetros de Java
            // params = {string: $scope.DtoSearch}; //Ver como se llaman los parametros de Java
            $http.get('/dlc/search/'+$scope.DtoSearch) //Conseguir URI
               .then(function (response) {
                   $scope.busquedaHecha = true;
                   if (!response.data.length){
                       $scope.respuestaBusqueda = false;
                   }else{
                       $scope.Lista = response.data;
                       $scope.respuestaBusqueda = true;
                   }

                });   
            //$scope.Lista = [{docName: "hola" , vUrl: "algo"}];
        };

    }
);
               
               
    app.controller('IndexerController',
    function ($scope, $http) {

        $scope.Titulo = 'Indexar Documentos';  // inicia mostrando el Listado
        // articulo cargado inicialmente, como demo para probar la interface visual (luego comentar esta linea)

        ///**FUNCIONES**///

        $scope.Indexar = function () {
            $http.get('/dlc/indexar')
                .then(function (response) {
                    $scope.Response = response.data.Response;
                });
        };
    }
    );

    app.controller('AgregarNewController',
    function ($scope, $http) {

        $scope.Titulo = 'Indexar Documentos';  // inicia mostrando el Listado
        // articulo cargado inicialmente, como demo para probar la interface visual (luego comentar esta linea)

        ///**FUNCIONES**///
        $scope.Indexar = function () {   
            params = {url: $scope.DtoSearch}; //Ver como puedo conseguir el Path del archivo
            $http.get('/dlc') //Agregar URI
                .then(function (response) {
                    $scope.Response = response.data.Response;  // variable para luego imprimir                   
                });
        };
        

    }
);
