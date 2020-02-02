SELECT 
  context_contract.ac_id, 
  context_contract.cc_id, 
  context_contract.cc_name, 
  context_parameter.cp_id, 
  context_parameter.cp_name, 
  context_parameter.bound_id, 
  context_parameter.bound_value, 
  context_argument.ca_id, 
  context_argument.variable_cp_id, 
  context_contract.owner_id, 
  closed_arguments_context_contract.target_cc_id
FROM 
  public.context_contract, 
  public.context_parameter, 
  public.context_argument, 
  public.closed_arguments_context_contract
WHERE 
  context_contract.ac_id = context_parameter.ac_id AND
  context_contract.cc_id = context_argument.cc_id AND
  context_argument.variable_cp_id = context_parameter.cp_id AND
  context_argument.ca_id = closed_arguments_context_contract.ca_id
ORDER BY
  context_contract.cc_id ASC;
