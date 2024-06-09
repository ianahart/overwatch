import { AiOutlineCheck } from 'react-icons/ai';
import { IPackage } from '../../interfaces';

export interface IPckgsProps {
  basic: IPackage;
  standard: IPackage;
  pro: IPackage;
}

const Pckgs = ({ basic, standard, pro }: IPckgsProps) => {
  return (
    <div className="border  p-4 border-gray-800 border-l-0 border-r-0">
      <h3 className="text-gray-400 text-lg">Packages</h3>
      <table>
        <tbody>
          <tr>
            {basic.price !== null && (
              <th>
                Basic <span className="text-green-400">${basic.price}</span>
              </th>
            )}
            {standard.price !== null && (
              <th>
                Standard <span className="text-green-400">${standard.price}</span>
              </th>
            )}
            {pro.price !== null && (
              <th>
                Pro <span className="text-green-400">${pro.price}</span>
              </th>
            )}
          </tr>
          <tr>
            <td>
              <p>Description:</p>
              <p>{basic.description}</p>
            </td>
            <td>
              <p>Description:</p>
              <p>{standard.description}</p>
            </td>
            <td>
              <p>Description:</p>
              <p>{pro.description}</p>
            </td>
          </tr>
          <tr>
            <td>
              <ul className="list-none">
                {basic.items.map(({ id, name }) => {
                  return (
                    <li key={id} className="flex my-1">
                      <AiOutlineCheck className="text-green-400 w-5" />
                      <p className="text-xs">{name}</p>
                    </li>
                  );
                })}
              </ul>
            </td>
            <td>
              <ul className="list-none">
                {standard.items.map(({ id, name }) => {
                  return (
                    <li key={id} className="flex  my-1">
                      <AiOutlineCheck className="text-green-400 w-5" />
                      <p className="text-xs">{name}</p>
                    </li>
                  );
                })}
              </ul>
            </td>
            <td>
              <ul className="list-none">
                {pro.items.map(({ id, name }) => {
                  return (
                    <li key={id} className="flex my-1">
                      <AiOutlineCheck className="text-green-400 w-5" />
                      <p className="text-xs">{name}</p>
                    </li>
                  );
                })}
              </ul>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default Pckgs;
