
/**
 * FakeEndServicesCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package br.ufc.storm.backend;

    /**
     *  FakeEndServicesCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class FakeEndServicesCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public FakeEndServicesCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public FakeEndServicesCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for removeFile method
            * override this method for handling normal response from removeFile operation
            */
           public void receiveResultremoveFile(
                    br.ufc.storm.backend.FakeEndServicesStub.RemoveFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeFile operation
           */
            public void receiveErrorremoveFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for runFile method
            * override this method for handling normal response from runFile operation
            */
           public void receiveResultrunFile(
                    br.ufc.storm.backend.FakeEndServicesStub.RunFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from runFile operation
           */
            public void receiveErrorrunFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for renameFile method
            * override this method for handling normal response from renameFile operation
            */
           public void receiveResultrenameFile(
                    br.ufc.storm.backend.FakeEndServicesStub.RenameFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from renameFile operation
           */
            public void receiveErrorrenameFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addFile method
            * override this method for handling normal response from addFile operation
            */
           public void receiveResultaddFile(
                    br.ufc.storm.backend.FakeEndServicesStub.AddFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addFile operation
           */
            public void receiveErroraddFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for instantiate method
            * override this method for handling normal response from instantiate operation
            */
           public void receiveResultinstantiate(
                    br.ufc.storm.backend.FakeEndServicesStub.InstantiateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from instantiate operation
           */
            public void receiveErrorinstantiate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFile method
            * override this method for handling normal response from getFile operation
            */
           public void receiveResultgetFile(
                    br.ufc.storm.backend.FakeEndServicesStub.GetFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFile operation
           */
            public void receiveErrorgetFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getStatus method
            * override this method for handling normal response from getStatus operation
            */
           public void receiveResultgetStatus(
                    br.ufc.storm.backend.FakeEndServicesStub.GetStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getStatus operation
           */
            public void receiveErrorgetStatus(java.lang.Exception e) {
            }
                


    }
    