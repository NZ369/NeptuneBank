import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICurrency, defaultValue } from 'app/shared/model/currency.model';

export const ACTION_TYPES = {
  FETCH_CURRENCY_LIST: 'currency/FETCH_CURRENCY_LIST',
  FETCH_CURRENCY: 'currency/FETCH_CURRENCY',
  RESET: 'currency/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICurrency>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CurrencyState = Readonly<typeof initialState>;

// Reducer

export default (state: CurrencyState = initialState, action): CurrencyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CURRENCY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CURRENCY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CURRENCY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CURRENCY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/currencies';

// Actions

export const getEntities: ICrudGetAllAction<ICurrency> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CURRENCY_LIST,
    payload: axios.get<ICurrency>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICurrency> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CURRENCY,
    payload: axios.get<ICurrency>(requestUrl)
  };
};

// export const createEntity: ICrudPutAction<ICurrency> = entity => async dispatch => {
//   const result = await dispatch({
//     type: ACTION_TYPES.CREATE_CURRENCY,
//     payload: axios.post(apiUrl, cleanEntity(entity))
//   });
//   return result;
// };

// export const updateEntity: ICrudPutAction<ICurrency> = entity => async dispatch => {
//   const result = await dispatch({
//     type: ACTION_TYPES.UPDATE_CURRENCY,
//     payload: axios.put(apiUrl, cleanEntity(entity))
//   });
//   return result;
// };

// export const deleteEntity: ICrudDeleteAction<ICurrency> = id => async dispatch => {
//   const requestUrl = `${apiUrl}/${id}`;
//   const result = await dispatch({
//     type: ACTION_TYPES.DELETE_CURRENCY,
//     payload: axios.delete(requestUrl)
//   });
//   return result;
// };

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
