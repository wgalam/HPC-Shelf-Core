
/**
 * BackEndServicesImplServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package br.ufc.mdcc.hpcshelf.backendservices.proxy;

    /**
     *  BackEndServicesImplServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class BackEndServicesImplServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public BackEndServicesImplServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public BackEndServicesImplServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for destroyPlatform method
            * override this method for handling normal response from destroyPlatform operation
            */
           public void receiveResultdestroyPlatform(
                    br.ufc.mdcc.hpcshelf.backendservices.proxy.BackEndServicesImplServiceStub.DestroyPlatformResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from destroyPlatform operation
           */
            public void receiveErrordestroyPlatform(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deployContractCallback method
            * override this method for handling normal response from deployContractCallback operation
            */
           public void receiveResultdeployContractCallback(
                    br.ufc.mdcc.hpcshelf.backendservices.proxy.BackEndServicesImplServiceStub.DeployContractCallbackResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deployContractCallback operation
           */
            public void receiveErrordeployContractCallback(java.lang.Exception e) {
            }
                


    }
    